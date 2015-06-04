package au.org.ala.regions

import grails.transaction.Transactional

@Transactional
class VisualisationService {
    def speciesIpService

    def getSpecies(String regionFid, String regionType, String regionName, String regionPid, String groupName, Boolean isSubgroup = false, String from = null, String to = null, String pageIndex = '0', String limit = "-1") {
        speciesIpService.byRegion()

    }
}
