package au.org.ala.regions

import grails.converters.JSON

class VisualisationController {
    def index() {}

    def test() {
        def data = [
                "totalPatentByRegion": [
                        ["Region", "Number"],
                        ["Indigenous Protected Areas", 100],
                        ["Angas Downs", 5]
                ],
                'patentsByStatus'    : [
                        ['Status', 'Number'],
                        ['Filed', 2],
                        ['Granted', 3]
                ],
                "patentsByDecade"    : [
                        ["Period", "Number"],
                        ["1990-2000", 1],
                        ["2000-2010", 1],
                        ["2010-2020", 3]
                ],
                "top20Applicants"    : [
                        ['Entity', 'Number'],
                        ['Elcelyx Therapeutics, Inc.', 1],
                        ['Colgate-Palmolive Company', 4]
                ]
        ];

        render(text: data as JSON, contentType: 'application/json')
    }

    SpeciesIpService speciesIpService
    PatentService patentService

    def visualisation() {
        def result =  [:]
        def patentsList = []
        def species
        def patents

        // Check the species name to determine whether should search for a species or a range/region
        if (!params.speciesName){ // Searches for the region
            species = speciesIpService.getSpecies(params.regionFid, params.regionType, params.regionName, params.regionPid, params.subgroup ?: params.group, params.subgroup ? true : false, params.from, params.to, params.pageIndex ?: "0")
            patents = species.records.collect() { sp -> sp.patents }

        }else{ // Search for only one species
            patents =  patentService.fetch(params.speciesName)
        }

        patents.each { it -> patentsList.addAll(it) }

        //remove duplicate patents
        patentsList = patentsList.unique { patent -> patent.applicationNumber }

        //Patents by year
        result << ["patentByYear": [["Period", "Number"]] + patentsList.countBy {
            it.filingDate[0..3].toInteger()
        }.collect { k, v -> [k, v] }]

        //Patents by status
        result << ["patentsByStatus": [["Status", "Number"]] + patentsList.countBy {
            it.filingStatus
        }.collect { k, v -> [k, v] }]

        // Top 20 Applicants
        def applicants = patentsList.countBy { it.applicants }.sort { -it.value }
        def i = 0
        result << ["top20Applicants": [["Entity", "Number"]] + applicants.findAll {
            i++ < 20
        }.collect { k, v -> [k, v] }]

        render result as JSON
    }
}
