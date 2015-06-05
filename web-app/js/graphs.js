/**
 * Created by temi varghese on 3/06/15.
 */


function Graphs(options) {
    var config = $.extend({
        bieUrl: 'http://bie.ala.org.au/ws/species/',
        params: {
            regionFid: undefined,
            regionType: undefined,
            regionName: undefined,
            regionPid: undefined,
            speciesName: undefined,
            group: 'ALL_SPECIES',
            subgroup: undefined
        }
    }, options);
    var id = config.id;
    var url = config.url;
    var graphs = this;

    this.update = function (params) {
        ['#patentsByStatus', '#top20Applicants', '#patentsByDecade'].forEach(function (item) {
            $(item).html('');
        });
        $.ajax({
            url: url,
            method: 'POST',
            data: params,
            success: function (data) {
                console.log(typeof data);

                graphs.drawColumnChart(data.patentsByStatus, {
                    title: 'Status of Patents',
                    id: 'patentsByStatus',
                    height:200,
                    width:'100%',
                    hAxis:{
                        slantedText:true,
                        slantedTextAngle:45
                    }
                });
                graphs.drawColumnChart(data.top20Applicants, {
                    title: 'Top 20 applicants',
                    id: 'top20Applicants',
                    height:200,
                    width:'100%',
                    hAxis:{
                        slantedText:true,
                        slantedTextAngle:45
                    }
                });
                graphs.drawColumnChart(data.patentByYear, {
                    title: 'Patents by filing date',
                    id: 'patentsByDecade',
                    height:200,
                    width:'100%',
                    hAxis:{
                        slantedText:true,
                        slantedTextAngle:45
                    }
                });
            }
        });
    }

    //event registration
    $(document).on('groupSelected', function (e, data) {
        console.log('group selected');
        console.log(data);
        var params = config.params;
        params.group = data.group;
        params.speciesName = undefined;
        params.subgroup = undefined;
        graphs.update(params);
    });

    $(document).on('subgroupSelected', function (e, data) {
        console.log('subgroupSelected selected');
        console.log(data);

        var params = config.params;
        params.group = data.group;
        params.speciesName = undefined;
        params.subgroup = data.subgroup;
        graphs.update(params);
    });

    $(document).on('speciesSelected', function (e, data) {
        console.log('speciesSelected selected');
        console.log(data);

        var params = config.params;
        params.group = data.group;
        params.speciesName = data.speciesName;
        params.subgroup = data.subgroup;
        graphs.update(params);

        graphs.getProfile(data);
    });

    this.drawPieChart = function (data, options) {
        data = google.visualization.arrayToDataTable(data);
        console.log(data);
        var opt = $.extend({
            title: 'Test Example'
        }, options);
        var chart = new google.visualization.PieChart(document.getElementById(options.id));
        chart.draw(data, opt);
    };

    this.drawBarChart = function (data, opt) {
        data = google.visualization.arrayToDataTable(data);

        var chart = new google.visualization.BarChart(document.getElementById(opt.id));
        chart.draw(data, opt);
    };

    this.drawColumnChart = function (data, opt) {
        data = google.visualization.arrayToDataTable(data);

        var chart = new google.visualization.ColumnChart(document.getElementById(opt.id));
        chart.draw(data, opt);
    };

    this.getProfile = function (data) {
        var params = $.extend({},config.params);
        params = $.extend(params, data);
        params.name = data.speciesName;
//        delete params.speciesName;
        ['#'+config.profileId, '#'+config.speciesProfileId].forEach(function (item) {
            $(item).html('');
        });
        $.ajax({
            url: config.profileUrl,
            method: 'GET',
            data: params,
            success: function (dp) {
                var record = dp.records[0]
                dp.lsid = record.guid;
                $.ajax({
                    url:config.bieUrl+record.guid+'.json',
//                    method:'JSONP',
                    dataType:'JSONP',
                    success: function(profile){
                        var img = profile.images || [];
                        if(img[0]){
                            img = img[0].repoLocation
                        } else {
                            img = '';
                        }
                        var record = dp.records[0]
                        record.img = img;
                        var profileData = tmpl(config.speciesTmplId, record);
                        $('#'+config.speciesProfileId).html(profileData);
                    }
                })
                var patents = dp.records[0].patents;
                var profileData = tmpl(config.tmplId, {patents: patents});
                $('#'+config.profileId).html(profileData);
            }
        })
    }

    //init graphs
    this.update(config.params);
}


// Simple JavaScript Templating
// John Resig - http://ejohn.org/ - MIT Licensed
(function(){
    var cache = {};

    this.tmpl = function tmpl(str, data){
        // Figure out if we're getting a template, or if we need to
        // load the template - and be sure to cache the result.
        var fn = !/\W/.test(str) ?
            cache[str] = cache[str] ||
                tmpl(document.getElementById(str).innerHTML) :

            // Generate a reusable function that will serve as a template
            // generator (and which will be cached).
            new Function("obj",
                    "var p=[],print=function(){p.push.apply(p,arguments);};" +

                    // Introduce the data as local variables using with(){}
                    "with(obj){p.push('" +

                    // Convert the template into pure JavaScript
                    str
                        .replace(/[\r\t\n]/g, " ")
                        .split("<%").join("\t")
                        .replace(/((^|%>)[^\t]*)'/g, "$1\r")
                        .replace(/\t=(.*?)%>/g, "',$1,'")
                        .split("\t").join("');")
                        .split("%>").join("p.push('")
                        .split("\r").join("\\'")
                    + "');}return p.join('');");

        // Provide some basic currying to the user
        return data ? fn( data ) : fn;
    };
})();

$('#patents_tmpl').html(
    '<table class="table table-condensed table-striped">\
        <th colspan="3">Patent List</th>\
        <% for (var i = 0; i < patents.length; i++) { %>\
        <tr><td colspan="3"><a target="_blank" href="http://pericles.ipaustralia.gov.au/ols/auspat/applicationDetails.do?applicationNo=<%=\
            patents[i].applicationNumber %>"><%=patents[i].title %> </a><div class="label link" onclick="openDetails(<%=i%>)">...</div></td></tr>\
            <tr class="hide" id="<%=i %>"><td style="border-top-width:0px;"><strong>Inventors:</strong> <%=patents[i].inventors %></td><td  style="border-top-width:0px;"><strong>Status:</strong><%=patents[i].filingStatus %></td><td  style="border-top-width:0px;"><strong>Applicants:</strong><%=patents[i].applicants %></td></tr>\
        <% } %>\
    </table>\
    '
);

$('#species_tmpl').html(
    '<table width="100%" class="table borderless">' +
        "<th>Species Profile</th>"+
        '<tr>' +
            '<td><label>Name:</label></td><td><a target="_blank" href="http://bie.ala.org.au/species/<%=guid%>"><%=name%></a></td>' +
            '<td rowspan="4"><img src="<%=img%>" style="height:200px"></td>' +
        '</tr>' +
        '<tr>'+
            '<td><label>Common name:</label></td><td><%=commonName%></td>' +
        '</tr>'+
        '<tr>'+
            '<td><label>Occurrence records:</label></td><td><%=count%></td>' +
        '</tr>'+
        '<tr>'+
            '<td><label>Patent count:</label></td><td><%=patentCount%></td>' +
        '</tr>'+
    '</table>'
);

function openDetails(id){
    $('#'+id).toggle('hide');
    return false;
}
