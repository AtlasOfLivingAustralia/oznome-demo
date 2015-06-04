package au.org.ala.regions

import grails.converters.JSON

class VisualisationController {

    PatentService patentService
    SpeciesIpService speciesIpService

    def visualisation() {
        def result = []
        def patentsList = []

        if (params.regionFid)

        def species = speciesIpService.getSpecies(params.regionFid, params.regionType, params.regionName, params.regionPid, params.subgroup ?: params.group, params.subgroup ? true : false, params.from, params.to, params.pageIndex ?: "0")

        def patents = species.records.collect() { sp -> sp.patents }

        patents.each { it -> patentsList.addAll(it) }

        //remove duplicate patents
        patentsList = patentsList.unique { patent -> patent.applicationNumber }

        //Patents by year
        result += ["patentByYear", [["Period", "Number"]] + patentsList.countBy {
            it.filingDate[0..3].toInteger()
        }.collect { k, v -> [k, v] }]

        //Patents by status
        result += ["patentsByStatus", [["Status", "Number"]] + patentsList.countBy {
            it.filingStatus
        }.collect { k, v -> [k, v] }]

        // Top 20 Applicants
        def applicants = patentsList.countBy { it.applicants }.sort { -it.value }
        def i = 0
        result += ["top20Applicants", [["Entity", "Number"]] + applicants.findAll {
            i++ < 20
        }.collect { k, v -> [k, v] }]

        render result as JSON
    }
}
