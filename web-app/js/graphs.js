/**
 * Created by temi varghese on 3/06/15.
 */


function Graphs(options) {
    var config = $.extend({
        params: {
            regionFid: undefined,
            regionType: undefined,
            regionName: undefined,
            regionPid: undefined,
            taxon_concept_lsid: undefined,
            group: 'ALL_SPECIES',
            subgroup: undefined
        }
    }, options);
    var id = config.id;
    var url = config.url;
    var graphs = this;

    this.update = function (params) {
        ['#patentsByStatus', '#top20Applicants', '#patentsByDecade'].forEach(function(item){
            $(item).html('');
        });
        $.ajax({
            url: url,
            method: 'GET',
            data: params,
            success: function (data) {
                console.log(typeof data);

                graphs.drawColumnChart(data.patentsByStatus, {
                    title: 'Status of Patents',
                    id: 'patentsByStatus'
                });
                graphs.drawColumnChart(data.top20Applicants, {
                    title: 'Top 20 applicants',
                    id: 'top20Applicants'
                });
                graphs.drawColumnChart(data.patentsByDecade, {
                    title: 'Patents by filing date',
                    id: 'patentsByDecade'
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
        params.taxon_concept_lsid = undefined;
        params.subgroup = undefined;
        graphs.update(params);
    });

    $(document).on('subgroupSelected', function (e, data) {
        console.log('subgroupSelected selected');
        console.log(data);

        var params = config.params;
        params.group = data.group;
        params.taxon_concept_lsid = undefined;
        params.subgroup = data.subgroup;
        graphs.update(params);
    });

    $(document).on('speciesSelected', function (e, data) {
        console.log('speciesSelected selected');
        console.log(data);

        var params = config.params;
        params.group = data.group;
        params.taxon_concept_lsid = data.speciesName;
        params.subgroup = data.subgroup;
        graphs.update(params);

        graphs.profile(data);
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

    //init graphs
    this.update(config.params);
}