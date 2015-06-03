package au.org.ala.regions

import grails.transaction.Transactional

/**
 * Get a list of species and annotate them with patent information
 */
@Transactional
class SpeciesIpService {
    def metadataService
    def patentService

    /**
     * Get the patents associated with the species in the region
     *
     * @param regionType Region type name
     * @param regionName Region name
      * @param summary If true, only build statistics
     *
     * @return A list of species, sorted by patent count (most first)
     */
    def byRegion(String regionType, String regionName, summary) {
        def regionMetadata = metadataService.regionMetadata(regionType, regionName)
        def fid = metadataService.fidFor(regionType)
        def pid = regionMetadata.pid
        log.info "Region type: ${regionType}, Region name: ${regionName}, Fid: ${fid}, Pid: ${pid}"
        def allSpecies = metadataService.getSpecies(fid, regionType, regionName, pid, null, false, null, null, "0", "-1")
        def names = allSpecies.records.collect { sp -> sp.name }
        names.unique()
        log.info "Getting ${names.size()} species for ${regionName}"
        def patents = patentService.fetchAll(names)
        def ip = allSpecies.records.collect { sp ->
            def pats = patents[sp.name] ?: []
            def sd = [
                    name: sp.name,
                    lsid: sp.guid,
                    commonName: sp.commonName,
                    occurrenceCount: sp.count,
                    patentCount: pats.size()
             ]
            if (!summary)
                sd.patents = pats
            sd
        }
        ip = ip.sort { - it.patentCount }
        return [
                count: ip.size(),
                species: ip
        ]
    }
}
