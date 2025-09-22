document.addEventListener("DOMContentLoaded", () => {
    scrollStep5 = scrollArea5.clientHeight-60;
    size5 = Math.floor((scrollArea5.offsetHeight-35)/30)*2;

    clickRow5();
    showScrollArea5();
});

function getPage5(){
    const searchText = document.getElementById('searchReceipt').value;

    $.ajax({
        url:'/api/getReceiptPage',
        method:'GET',
        data: {page:page5, size:size5, searchText:searchText},
        success:function(data){
            totalPages5=data.page.totalPages;
            if(totalPages5<page5) finPage5=true;
            const contents = data.content;

            let colorFlag = true;
            contents?.forEach(content => {
                const newTr = document.createElement('tr');
                newTr.id = 'receipt'+content.orderId;
                if(colorFlag) newTr.className = 'cor-wg';
                colorFlag = !colorFlag;

                [formatDateTime(content.finishTime), content.receiptNumber, (content.price+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원', (content.status===true)?'':'취소'].forEach(text => {
                    const newTd = document.createElement('td');
                    newTd.textContent = text;
                    newTr.appendChild(newTd);
                });

                fifthInsertPoint.appendChild(newTr);
            });
            updateButtonColors5();
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
function searchReceipt(){
    fifthInsertPoint.innerHTML = '';
    page5=1;
    finPage5=false;
    getPage5();
}
function resetReceiptPage(){
    fifthInsertPoint.innerHTML = '';
    page5=1;
    finPage5=false;
    document.getElementById('searchReceipt').value='';
}
function search(){
    searchReceipt();
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
                selectedRow5 = row.id.slice(7);
                openReceiptModal();
            }else{
                row.style.backgroundColor = '';
                selectedRow5 = '';
                closeReceiptModal();
            }
        }
    });
}