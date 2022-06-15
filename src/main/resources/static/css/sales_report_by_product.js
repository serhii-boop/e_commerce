var chartOptions;
var totalSales;
var totalItems;
var startDateField;
var endDateField;
var MILLISECONDS_A_DAY = 24 * 60 * 60 * 1000

function loadSalesReportByDateForProduct(period) {

    if (period == "custom") {
        startDate = $("#startDate_product").val();
        endDate = $("#endDate_product").val();
        requestURL = "/admin-page/report/product/" + startDate + "/" + endDate;
    } else {
        requestURL = "/admin-page/report/product/" + period;
    }

    $.get(requestURL, function(responseJSON) {
        prepareChartDateForProduct(responseJSON);

    });
}


function prepareChartDateForProduct(responseJSON) {

    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Product');
    data.addColumn('number', 'Quantity');
    data.addColumn('number', 'Total sales (UAH)');


    totalSales = 0.0;
    totalItems = 0;
    $.each(responseJSON, function(index, pata) {
        data.addRows([[pata.ident, pata.productsCount, pata.sales]]);
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
        height: 360, width: '100%',
        showRowNumber: true,
        page: 'enable',
        sortColumn: 1,
        sortAscending: false
    };


    var salesChart = new google.visualization.Table(document.getElementById('chart_sales_by_product'));
    salesChart.draw(data, chartOptions);
    $("#textTotalSales_product").text(totalSales + " - UAH")
    if (period == "last_7_days") {
        $("#textAvgSales_product").text((Math.round(totalSales * 100 / 7) / 100) + " - UAH")
    }
    if (period == "last_month") {
        $("#textAvgSales_product").text((Math.round(totalSales * 100 / 30) / 100) + " - UAH")
    }
    if (period == "last_6_month") {
        $("#textAvgSales_product").text((Math.round(totalSales * 100 / 6) / 100) + " - UAH")
    }
    if (period == "last_year") {
        $("#textAvgSales_product").text((Math.round(totalSales * 100 / 12) / 100) + " - UAH")
    }
    if (period == "custom") {
        $("#textAvgSales_product").text((Math.round(totalSales * 100 / calculateDays()) / 100) + " - UAH")
    }

    $("#textTotalItems_product").text(totalItems)
}




