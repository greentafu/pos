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
    const posNumber = document.getElementById('findPosNumber').value;
    const receiptNumber = document.getElementById('receiptNumber').value;
    const searchText = document.getElementById('searchName').value;

    $.ajax({
        url:'/api/getOrderListPage',
        method:'GET',
        data: {page:page5, size:size5, startDate:startDate, endDate:endDate, posNumber:posNumber,
                receiptNumber:receiptNumber, searchText:searchText},
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

                let name = content.product;
                if(content.count>1) name += ' 외 '+ formatComma(content.count)+'개';
                [formatDateTime(content.date), name, formatComma(content.price), formatComma(content.discount1+content.discount2),
                formatComma(content.totalPayment), formatComma(content.cash), formatComma(content.card), formatComma(content.point),
                content.pos, (content.status)?'':'취소'].forEach(text => {
                    const newTd = document.createElement('td');
                    newTd.textContent = text;
                    newTr.appendChild(newTd);
                });

                fifthInsertPoint.appendChild(newTr);
            });
            updateButtonColors5();

            document.getElementById('summaryTotalCount').textContent = formatComma(summary.orderCount)+'건';
            document.getElementById('summaryPrice').textContent = formatComma(summary.price);
            document.getElementById('summaryDiscount').textContent = formatComma(summary.discount1+summary.discount2);
            document.getElementById('summaryPayment').textContent = formatComma(summary.totalPayment);
            document.getElementById('summaryCash').textContent = formatComma(summary.cash);
            document.getElementById('summaryCard').textContent = formatComma(summary.card);
            document.getElementById('summaryPoint').textContent = formatComma(summary.point);
            document.getElementById('summaryStatusCount').textContent = formatComma(summary.statusCount)+'건';
        },
        error: function(xhr) { console.log('error'); }
    });
    page5++;
}
function formatDateTime(tempDate) {
    const pad = n => n.toString().padStart(2, '0');

    const date = new Date(tempDate);
    const yyyy = date.getFullYear();
    const MM = pad(date.getMonth() + 1);
    const dd = pad(date.getDate());
    const HH = pad(date.getHours());
    const mm = pad(date.getMinutes());
    const ss = pad(date.getSeconds());
    return `${yyyy}-${MM}-${dd} ${HH}:${mm}:${ss}`;
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
                selectedRow5 = row.id.slice(5);
                openReceiptModal();
            }else{
                row.style.backgroundColor = '';
                selectedRow5 = '';
                closeReceiptModal();
            }
        }
    });
}