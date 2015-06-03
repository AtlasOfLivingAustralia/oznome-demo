/**
 * Created by temi varghese on 3/06/15.
 */


function graphs(options){
    var config = $.extend({},options);
    var id = config.id;
    var url = config.url;
    var graphs = this;
    $.ajax({
        url: url,
        method:'GET',
        success: function(data) {
            console.log(typeof data);
            graphs.drawPieChart(data.totalPatentByRegion, {
                title: 'Total Patents by region',
                id: 'totalPatentByRegion'
            });
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

    this.drawPieChart = function(data, options){
        data = google.visualization.arrayToDataTable(data);
        console.log(data);
        var opt = $.extend({
            title: 'Test Example'
        },options);
        var chart = new google.visualization.PieChart(document.getElementById(options.id));
        chart.draw(data, opt);
    };

    this.drawBarChart = function(data, opt){
        data = google.visualization.arrayToDataTable(data);

        var chart = new google.visualization.BarChart(document.getElementById(opt.id));
        chart.draw(data, opt);
    };

    this.drawColumnChart =  function(data, opt){
        data = google.visualization.arrayToDataTable(data);

        var chart = new google.visualization.ColumnChart(document.getElementById(opt.id));
        chart.draw(data, opt);
    };
}