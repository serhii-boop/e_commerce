var chartOptions;
var totalSales;
var totalOrders;
var startDateField;
var endDateField;
var MILLISECONDS_A_DAY = 24 * 60 * 60 * 1000

function loadSalesReportByDate(period) {

    if (period == "custom") {
        startDate = $("#startDate_date").val();
        endDate = $("#endDate_date").val();
        requestURL = "/admin-page/report/sales-by-date/" + startDate + "/" + endDate;
    } else {
        requestURL = "/admin-page/report/sales-by-date/" + period;
    }

    $.get(requestURL, function(responseJSON) {
        prepareChartDate(responseJSON);

    });
}


function prepareChartDate(responseJSON) {

    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Date');
    data.addColumn('number', 'Sales');
    data.addColumn('number', 'Total orders');

    totalSales = 0.0;
    totalOrders = 0.0;
    $.each(responseJSON, function(index, pata) {
        data.addRows([[pata.ident, pata.sales, pata.ordersCount]]);
        totalSales += parseFloat(pata.sales);
        totalOrders += parseInt(pata.ordersCount);
    });
    var texttt = "";
    if (period == "last_7_days") texttt = 'Sales in last 7 days';
    if (period == "last_month") texttt = 'Sales in last month';
    if (period == "last_6_month") texttt = 'Sales in last 6 month';
    if (period == "last_year") texttt = 'Sales in last year';
    if (period == "custom") texttt = 'Custom date range';

    chartOptions = {
        title: texttt,
        'height': 360,
        'width': 1000,
        legend: {position: 'top'},

        series: {
            0: {targetAxisIndex: 0},
            1: {targetAxisIndex: 1}
        },

        vAxes: {
            0: {title: 'Sales Amount'},
            1: {title: 'Number of Orders'}
        }
    };


    var salesChart = new google.visualization.ColumnChart(document.getElementById('chart_sales_by_date'));
    salesChart.draw(data, chartOptions);
    $("#textTotalSales_date").text(totalSales + " - UAH")
    if (period == "last_7_days") {
        $("#textAvgSales_date").text((Math.round(totalSales * 100 / 7) / 100) + " - UAH")
    }
    if (period == "last_month") {
        $("#textAvgSales_date").text((Math.round(totalSales * 100 / 30) / 100) + " - UAH")
    }
    if (period == "last_6_month") {
        $("#textAvgSales_date").text((Math.round(totalSales * 100 / 6) / 100) + " - UAH")
    }
    if (period == "last_year") {
        $("#textAvgSales_date").text((Math.round(totalSales * 100 / 12) / 100) + " - UAH")
    }
    if (period == "custom") {
        $("#textAvgSales_date").text((Math.round(totalSales * 100 / calculateDays()) / 100) + " - UAH")
    }

    $("#textTotalItems_date").text(totalOrders)
}




