var chartOptions;
var totalSales;
var totalItems;
var startDateField;
var endDateField;
var MILLISECONDS_A_DAY = 24 * 60 * 60 * 1000

function loadSalesReportByDateForCategory(period) {

    if (period == "custom") {
        startDate = $("#startDate_category").val();
        endDate = $("#endDate_category").val();
        requestURL = "/admin-page/report/category";
    } else {
        requestURL = "/admin-page/report/category/" + period;
    }

    $.get(requestURL, function(responseJSON) {
        prepareChartDateForCategory(responseJSON);

    });
}


function prepareChartDateForCategory(responseJSON) {

    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Category');
    data.addColumn('number', 'Sales');


    totalSales = 0.0;
    totalItems = 0;
    $.each(responseJSON, function(index, pata) {
        data.addRows([[pata.ident, pata.productsCount]]);
        totalSales += parseFloat(pata.sales);
        totalItems += parseInt(pata.productsCount);
    });
    var texttt = "";
    if (period == "last_7_days") texttt = 'Sales in last 7 days';
    if (period == "last_month") texttt = 'Sales in last month';
    if (period == "last_6_month") texttt = 'Sales in last 6 month';
    if (period == "last_year") texttt = 'Sales in last year';
    if (period == "custom") texttt = 'Custom date range';

    chartOptions = {
        height: 360, legend: {position: 'right'}
    };


    var salesChart = new google.visualization.PieChart(document.getElementById('chart_sales_by_category'));
    salesChart.draw(data, chartOptions);
    $("#textTotalSales_category").text(totalSales + " - UAH")
    if (period == "last_7_days") {
        $("#textAvgSales_category").text((Math.round(totalSales * 100 / 7) / 100) + " - UAH")
    }
    if (period == "last_month") {
        $("#textAvgSales_category").text((Math.round(totalSales * 100 / 30) / 100) + " - UAH")
    }
    if (period == "last_6_month") {
        $("#textAvgSales_category").text((Math.round(totalSales * 100 / 6) / 100) + " - UAH")
    }
    if (period == "last_year") {
        $("#textAvgSales_category").text((Math.round(totalSales * 100 / 12) / 100) + " - UAH")
    }
    if (period == "custom") {
        $("#textAvgSales_category").text((Math.round(totalSales * 100 / calculateDays()) / 100) + " - UAH")
    }

    $("#textTotalItems_category").text(totalItems)
}




