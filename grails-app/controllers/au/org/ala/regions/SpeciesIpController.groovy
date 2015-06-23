package au.org.ala.regions

import grails.converters.JSON

class SpeciesIpController {
    def speciesIpService

    def region() {
        def summary = params.summary?.toBoolean() ?: false
        def ip = speciesIpService.byRegion(params.regionType, params.regionName, summary)

        render ip as JSON
    }

    def list() {
        def species = speciesIpService.getSpecies(params.regionFid, params.regionType, params.regionName, params.regionPid, params.subgroup?:params.group, params.subgroup ? true : false, params.from, params.to, params.pageIndex ?: "0")

        render species as JSON
    }


    def species() {
        def species = speciesIpService.getSingleSpecies(params.regionFid, params.regionType, params.regionName, params.regionPid, params.name, params.from, params.to)

        render species as JSON
    }
}
