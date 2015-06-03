<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="${grailsApplication.config.layout.skin?:'main'}"/>
    <title>${region.name} | Atlas of Living Australia</title>
    <r:require modules="region"/>
</head>
<body class="nav-locations">

<div class="row">
    <div class="span12">
        <ul class="breadcrumb pull-left">
            <rg:breadcrumbTrail/>
            <li><a href="${grailsApplication.config.grails.serverURL}#rt=${region.type}">Regions</a> <span class="divider"><i class="fa fa-arrow-right"></i></span></li>
            <g:if test="${region.parent}">
                <li><a href="${grailsApplication.config.grails.serverURL}/${region.parent.type}/${region.parent.name}">${region.parent.name}</a> <span class="divider"><i class="fa fa-arrow-right"></i></span></li>
                <g:if test="${region.parent.child}">
                    <li><a href="${grailsApplication.config.grails.serverURL}/${region.parent.child.type}/${region.parent.child.name}">${region.parent.child.name}</a> <span class="divider"><i class="fa fa-arrow-right"></i></span></li>
                </g:if>
            </g:if>
            <li class="active">${region.name}</li>
        </ul>
    </div>
</div>

<div class="row" id="emblemsContainer">
    <div class="span12">
        <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
        </g:if>
        <h1>Region name: <strong>${region.name}</strong></h1>
    </div>
</div>

<div class="row">
    <div class="span12">
        <h4 id="occurrenceRecords">ALA Occurrence records: <strong><span id="totalRecords">Counting...</span></strong> | Total patents:  <strong><span id="totalPatents">Counting...</span></strong></h4>
    </div>
</div>

<div class="row">
    <div class="span7">
        <ul class="nav nav-tabs" id="explorerTabs">
            <li class="active"><a id="speciesTab" href="#speciesTabContent" data-toggle="tab">Species <i class="fa fa-cog fa-spin fa-lg hidden"></i></a></li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane active" id="speciesTabContent">
                <table class="table table-condensed table-hover" id="groups">
                    <thead>
                        <tr>
                            <th class="text-center">Group</th>
                        </tr>
                    </thead>
                    <aa:zone id="groupsZone" tag="tbody" href="${g.createLink(controller: 'region', action: 'showGroups', params: [regionFid: region.fid,regionType: region.type, regionName: region.name, regionPid: region.pid])}"
                             jsAfter="regionWidget.groupsLoaded();">
                        <tr class="spinner">
                            <td class="spinner text-center">
                                <i class="fa fa-cog fa-spin fa-2x"></i>
                            </td>
                        </tr>
                    </aa:zone>
                </table>
                <table class="table table-condensed table-hover" id="species">
                    <thead>
                        <tr>
                            <th colspan="2" class="text-center">Species</th>
                            <th class="text-right">Records</th>
                            <th class="text-right">Patents</th>
                        </tr>
                    </thead>
                    <aa:zone id="speciesZone" tag="tbody" jsAfter="regionWidget.speciesLoaded();">
                        <tr class="spinner">
                            <td colspan="3" class="spinner text-center">
                                <i class="fa fa-cog fa-spin fa-2x"></i>
                            </td>
                        </tr>
                    </aa:zone>
                </table>
            </div>
        </div>
    </div>

    <div class="span6" id="tabs" role="tabpanel">

        <ul class="nav nav-tabs" id="controlsMapTab" role="tablist">
            <li class="active" role="presentation">
                <a href="#tab-map"  aria-controls="profile" role="tab" data-toggle="tab">Map</a>
            </li>
            <li role="presentation">
                <a href="#tab-graph"  aria-controls="profile" role="tab" data-toggle="tab">Graph</a>
            </li>
        </ul>

        <div class="tab-content">
            <div role="tabpanel" class="tab-pane active" id="tab-map">
                <div id="region-map"></div>

                <div class="accordion" id="opacityControls">
                    <div class="accordion-group">
                        <div class="accordion-heading">
                            <a class="accordion-toggle" data-toggle="collapse" href="#opacityControlsContent">
                                <i class="fa fa-chevron-right"></i>Map opacity controls
                            </a>
                        </div>
                        <div id="opacityControlsContent" class="accordion-body collapse">
                            <div class="accordion-inner">
                                <label class="checkbox">
                                    <input type="checkbox"name="occurrences" id="toggleOccurrences" checked> Occurrences
                                </label>
                                <div id="occurrencesOpacity"></div>
                                <label class="checkbox">
                                    <input type="checkbox" name="region" id="toggleRegion" checked> Region
                                </label>
                                <div id="regionOpacity"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div id="tab-graph" role="tabpanel" class="tab-pane">
                <div id="totalPatentByRegion"></div>
                <div id="patentsByStatus"></div>
                <div id="top20Applicants"></div>
                <div id="patentsByDecade"></div>
            </div>
        </div>
    </div>
</div>

<r:script>
    google.load("visualization", "1", {packages:["corechart"]});
    var regionWidget;

    $(function() {


        regionWidget = new RegionWidget({
            regionName: '${region.name}',
            regionType: '${region.type}',
            regionFid: '${region.fid}',
            regionPid: '${region.pid}',
            regionLayerName: '${region.layerName}',
            urls: {
                regionsApp: '${g.createLink(uri: '/', absolute: true)}',
                proxyUrl: '${g.createLink(controller: 'proxy', action: 'index')}',
                proxyUrlBbox: '${g.createLink(controller: 'proxy', action: 'bbox')}',
                speciesPageUrl: "${grailsApplication.config.bie.baseURL}/species/",
                biocacheServiceUrl: "${grailsApplication.config.biocache.baseURL}/ws",
                biocacheWebappUrl: "${grailsApplication.config.biocache.baseURL}",
                spatialWmsUrl: "${grailsApplication.config.spatial.baseURL}/geoserver/ALA/wms?",
                spatialCacheUrl: "${grailsApplication.config.spatial.baseURL}/geoserver/gwc/service/wms?",
                spatialServiceUrl: "${grailsApplication.config.layersService.baseURL}/",
            },
            username: '${rg.loggedInUsername()}',
            q: '${region.q}'
        });

        regionWidget.setMap(new RegionMap({
            bbox: {
                sw: {lat: ${region.bbox?.minLat}, lng: ${region.bbox?.minLng}},
                ne: {lat: ${region.bbox?.maxLat}, lng: ${region.bbox?.maxLng}}
            },
            useReflectService: ${useReflect}
        }));

        $("body").on("shown.bs.tab", "#tab-map", function() {
            regionWidget.getMap().invalidateSize();
        });
    });

    $( "#tabs" ).tab('show');
    google.setOnLoadCallback(function(){
        graphs({
            url:"http://localhost:8080/oznome-demo/vis"
        })
    });
</r:script>
</body>
</html>