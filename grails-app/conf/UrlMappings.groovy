class UrlMappings {

	static mappings = {

        "/patents" controller: "patent", action: [POST: "patents"]


        "/$regionType/$regionName" (controller: 'regions', action: 'region') {
            constraints {
                //do not match controllers
                regionType(matches:'(?!(^data\$|^proxy\$|^region\$|^regions\$|^visualisation\$)).*')
            }
        }
        
        "/$regionType" (controller: 'regions', action: 'regions') {
            constraints {
                //do not match controllers
                regionType(matches:'(?!(^data\$|^proxy\$|^region\$|^regions\$|^visualisation\$)).*')
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
