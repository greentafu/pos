document.addEventListener("DOMContentLoaded", () => {
    scrollStep5 = scrollArea5.clientHeight-60;
    size5 = Math.floor((scrollArea5.offsetHeight-35)/30)*2;

    getPage5();
    clickRow5();
    showScrollArea5();
});

function getPage5(){
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;
    const searchText = document.getElementById('searchName').value;

    $.ajax({
        url:'/api/getMenuListPage',
        method:'GET',
        data: {page:page5, size:size5, startDate:startDate, endDate:endDate, searchText:searchText},
        success:function(data){
            const pageDTO = data.pageDTO;
            const summary = data.summaryDTO;

            totalPages5=pageDTO.page.totalPages;
            if(totalPages5<page5) finPage5=true;
            const contents = pageDTO.content;

            let colorFlag = true;
            contents?.forEach(content => {
                const newTr = document.createElement('tr');
                newTr.id = 'order'+content.orderId;
                if(colorFlag) newTr.className = 'cor-wg';
                colorFlag = !colorFlag;

                const price = content.price;
                const discount = content.discount1+content.discount2;
                const totalPayment = content.totalPayment;
                const cost = content.cost1+content.cost2;
                const vat = totalPayment/11;
                const net = Math.round(totalPayment-vat);
                const profit = net-cost;
                const percent =  profit/net*100;

                [content.number, content.name, formatComma(content.count), formatComma(price), formatComma(discount),
                 formatComma(totalPayment), formatComma(vat), formatComma(net), formatComma(cost),
                 (profit<0)?'-'+formatComma(profit):formatComma(profit), Math.round(percent)+'%'].forEach(text => {
                    const newTd = document.createElement('td');
                    newTd.textContent = text;
                    newTr.appendChild(newTd);
                });

                fifthInsertPoint.appendChild(newTr);
            });
            updateButtonColors5();

            const price = summary.price;
            const discount = summary.discount1+summary.discount2;
            const totalPayment = summary.totalPayment;
            const cost = summary.cost1+summary.cost2;
            const vat = totalPayment/11;
            const net = Math.round(totalPayment-vat);
            const profit = net-cost;
            let percent =  profit/net*100;
            if(net===0) percent = 0;

            document.getElementById('summaryProductCount').textContent = summary.number;
            document.getElementById('summaryTotalCount').textContent = formatComma(summary.count);
            document.getElementById('summaryPrice').textContent = formatComma(price);
            document.getElementById('summaryDiscount').textContent = formatComma(discount);
            document.getElementById('summaryPayment').textContent = formatComma(totalPayment);
            document.getElementById('summaryVat').textContent = formatComma(vat);
            document.getElementById('summaryNet').textContent = formatComma(net);
            document.getElementById('summaryCost').textContent = formatComma(cost);
            document.getElementById('summaryProfit').textContent = (profit<0)?'-'+formatComma(profit):formatComma(profit);
            document.getElementById('summaryPercent').textContent = Math.round(percent)+'%';
        },
        error: function(xhr) { console.log('error'); }
    });
    page5++;
}
function search(){
    fifthInsertPoint.innerHTML = '';
    page5=1;
    finPage5=false;
    getPage5();
}

function clickRow5(){
    scrollArea5.addEventListener("click", (event) => {
        const rows = scrollArea5.querySelectorAll('table.history tbody tr');
        const row = event.target.closest('tbody tr');
        if(row!==null) {
            const color = row.style.backgroundColor;
            if(color==='') {
                rows.forEach(temp => temp.style.backgroundColor = '');
                row.style.backgroundColor = '#81ACEC';
            }else row.style.backgroundColor = '';
        }
    });
}