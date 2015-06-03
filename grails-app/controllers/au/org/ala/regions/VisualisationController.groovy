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

        render(text: data as JSON, contentType:'application/json')
    }
}
