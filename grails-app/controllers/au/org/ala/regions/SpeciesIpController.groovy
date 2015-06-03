package au.org.ala.regions

import grails.converters.JSON

class SpeciesIpController {
    def speciesIpService

    def region() {
        def summary = params.summary?.toBoolean() ?: false
        def ip = speciesIpService.byRegion(params.regionType, params.regionName, summary)

        render ip as JSON
    }
}
