package au.org.ala.regions

import com.gmongo.GMongo
import groovy.time.TimeCategory
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import javax.annotation.PostConstruct
import java.util.concurrent.ConcurrentHashMap

import static groovyx.gpars.GParsPool.withPool

class PatentService {

    static final String AUSPAT_URL = 'http://pericles.ipaustralia.gov.au/ols/auspat/advancedSearch.do?resultsPerPage=100&includeTextSearch=on&submit=Search&queryString='

    static final String SEARCH_RESULTS_TABLE = "#rawresults tbody tr.metadata"

    static final int THREADS = 5
    static final int MAX_CACHE_AGE_MINUTES = 60 * 24 * 7
    static final int CONNECT_TIMEOUT_MILLIS = 30000

    def database

    @PostConstruct
    def init() {
        database = new GMongo().getDB("patents")
    }

    def fetchAll(json) {
        Map<String, String> results = [:] as ConcurrentHashMap

        withPool(THREADS) {
            json.eachParallel {
                try {
                    def result = getCached(it)
                    if (result == null) {
                        result = fetch(it)
                    } else {
                        log.debug("${it} was cached")
                    }
                    results << [(it): result]
                } catch (Exception ex) {
                    def elt = ex.stackTrace[0]
                    log.error("Can't retrieve information for ${it}: ${ex.message}: ${elt.fileName}:${elt.lineNumber}")
                    results << [(it): []]
                }
            }
        }
        results
    }

    def fetch(String speciesName) {
        log.debug("Fetching ${speciesName}...")

        // search claims, title and abstract
        String query = "(\"${speciesName}\" IN CS) OR (\"${speciesName}\" IN TI) OR (\"${speciesName}\" IN AB)"
        query = URLEncoder.encode(query, "UTF-8")

        Document doc = Jsoup.connect("${AUSPAT_URL}${query}").timeout(CONNECT_TIMEOUT_MILLIS).cookie("hasAccepted", "true").get()

        def searchResults = doc.select(SEARCH_RESULTS_TABLE)

        List result = searchResults.collect {
            def columns = it.select("td")
            [
                    applicationNumber: columns[1].select("a").text(),
                    title            : columns[2].text(),
                    applicants       : columns[3].text(),
                    inventors        : columns[4].text(),
                    filingDate       : columns[5].text(),
                    filingStatus     : columns[6].text()
            ]
        }

        database.patents.insert([species: speciesName, results: result, cacheTimestamp: new Date()])

        result
    }

    def getCached(String speciesName) {
        def cached = database.patents.findOne(species: speciesName)

        if (cached) {
            def timeInCache = TimeCategory.minus(new Date(), cached.cacheTimestamp as Date)
            if (timeInCache.minutes > MAX_CACHE_AGE_MINUTES) {
                log.debug("${speciesName} was cached ${timeInCache.minutes} minutes ago - too old, removing from cache")
                database.patents.remove(species: speciesName)
                cached = null
            }
        }

        cached?.results
    }
}
