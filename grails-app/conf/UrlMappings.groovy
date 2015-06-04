class UrlMappings {

	static mappings = {

        "/patents" controller: "patent", action: [POST: "patents"]


        "/visualisation" controller: "visualisation", action: [POST: "visualisation"]


        "/$regionType/$regionName/patents" (controller: 'speciesIp', action: 'region') {
            constraints {
                //do not match controllers
                regionType(matches:'(?!(^data\$|^proxy\$|^region\$|^regions\$)).*')
            }
        }

        "/$regionType/$regionName" (controller: 'regions', action: 'region') {
            constraints {
                //do not match controllers
                regionType(matches:'(?!(^data\$|^proxy\$|^region\$|^regions\$)).*')
            }
        }
        
        "/$regionType" (controller: 'regions', action: 'regions') {
            constraints {
                //do not match controllers
                regionType(matches:'(?!(^data\$|^proxy\$|^region\$|^regions\$)).*')
            }
        }

		"/$controller/$action?/$id?(.$format)?"{
			constraints {
				// apply constraints here
			}
		}

		"/"(controller: 'regions')
		"500"(view:'/error')
	}
}
