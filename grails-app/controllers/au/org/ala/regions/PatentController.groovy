package au.org.ala.regions

import grails.converters.JSON

class PatentController {

    PatentService patentService

    def patents() {

        def json = request.getJSON()

        def result = patentService.fetchAll(json)

        render result as JSON
    }
}
