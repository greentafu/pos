let myChart1 = null;
let myChart2 = null;

let colors=['#E37E7E', '#E6F193', '#949FF5', '#A6F4A5', '#CC8DE9', '#A6EAF0', '#D9D9D9'];
let productName=[];
let yList=[];
let y1List=[];
let pieList=[];

document.addEventListener("DOMContentLoaded", () => {
    getPage();
});

function getPage(){
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    productName=[];
    yList=[];
    y1List=[];
    pieList=[];

    $.ajax({
        url:'/api/getMenuChartPage',
        method:'GET',
        data: {startDate:startDate, endDate:endDate},
        success:function(data){
            const mainList = data.mainList;
            const subList = data.subList;

            mainList?.forEach(dto => {
                const name = dto.name;
                const profit = dto.profit;
                const percent =  dto.percent;
                const totalPercent = dto.totalPercent;

                console.log(percent);

                productName.push(name);
                yList.push(profit/10000);
                y1List.push(percent.toFixed(2));
                pieList.push(totalPercent.toFixed(2));
            });

            showBarChart();
            showPieChart();
        },
        error: function(xhr) { console.log('error'); }
    });
}

// chart
const whiteBackgroundPlugin = {
    beforeDraw: (chart, args, options) => {
        const {ctx, chartArea: {left, top, width, height}} = chart;
        ctx.save();
        ctx.fillStyle = 'white';
        ctx.fillRect(left, top, width, height);
        ctx.restore();
    }
};
function showBarChart(){
    const canvas = document.getElementById('barChart');
    if (myChart1 !== null) myChart1.destroy();

    myChart1 = new Chart(canvas, {
        type: 'bar',
        data: {
            labels: productName,
            datasets: [
                {
                    label: '이익금',
                    data: yList,
                    order: 2,
                    yAxisID: 'y',
                    type: 'bar',
                    backgroundColor: colors
                },
                {
                    label: '이익률',
                    data: y1List,
                    order: 1,
                    yAxisID: 'y1',
                    type: 'line',
                    borderWidth: 1,
                    borderColor: 'black',
                    pointStyle: 'circle',
                    pointBackgroundColor : '#ECECEC'
                }
            ]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: { legend: { display: false }
            },
            scales: {
                x: {
                    ticks: {color: 'black'},
                    title: {color: 'black'},
                    grid: {display: false}
                },
                y: {
                    position: 'left',
                    min: 0,
                    max: 200,
                    ticks: { color: 'black', stepSize: 20, callback: value => `${value}` },
                    title: { color: 'black', display: true, text: '이익금(만원)' }
                },
                y1: {
                    position: 'right',
                    min: 0,
                    max: 100,
                    ticks: { color: 'black', stepSize: 10, callback: value => `${value}` },
                    title: { color: 'black', display: true, text: '이익률(%)' }
                }
            }
        },
        plugins: [whiteBackgroundPlugin]
    });

}
function showPieChart(){
    const canvas = document.getElementById('pieChart');
    if (myChart2 !== null) myChart2.destroy();

    myChart2 = new Chart(canvas, {
        type: 'pie',
        data: {
            labels: productName,
            datasets: [{
                label: '순이익률',
                data: pieList,
                backgroundColor: colors,
                hoverOffset: 4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { display: false},
                datalabels: {
                    formatter: (value, context) => {
                        if (value < 10) return ''
                        return context.chart.data.labels[context.dataIndex]+ '\n(' + value+'%)'
                    },
                    textAlign: 'center',
                    color: 'black'
                }
            }
        },
        plugins: [ChartDataLabels]
    });
}