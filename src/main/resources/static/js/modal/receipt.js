// open Modal
function openReceiptModal(){
    document.getElementById("modalItem").style.display = "block";
    const allModal = document.querySelectorAll('.receiptModal');
    allModal?.forEach(modal => modal.style.display='none');

    getReceipt(selectedRow5);
    document.getElementById('receiptModal').style.display='';
}
function openCashReceiptModal(){
    const checkboxes = document.querySelectorAll('input[type="checkbox"].selectReceipt');
    checkboxes.forEach(box => box.checked=false);

    getCashReceipt(selectedRow5);

    document.getElementById('showIssueCashList').classList.add('selected');
    document.getElementById('showNonIssueCashList').classList.remove('selected');
    document.getElementById('printCashReceipt').style.display = '';
    document.getElementById('cancelCashReceipt').style.display = '';
    document.getElementById('saveCashReceipt').style.display = 'none';

    document.getElementById('cashReceiptModal').style.display='';
}
function openNonCashReceipt(){
    const checkboxes = document.querySelectorAll('input[type="checkbox"].selectReceipt');
    checkboxes.forEach(box => box.checked=false);

    getNonCashReceipt(selectedRow5);

    document.getElementById('showIssueCashList').classList.remove('selected');
    document.getElementById('showNonIssueCashList').classList.add('selected');
    document.getElementById('printCashReceipt').style.display = 'none';
    document.getElementById('cancelCashReceipt').style.display = 'none';
    document.getElementById('saveCashReceipt').style.display = '';
}
function openSaveCashReceipt(){
    const checkboxes = document.querySelectorAll('input[type="checkbox"].selectReceipt');
    const idList = [];
    checkboxes.forEach(box => {
        if(box.checked === true){
            const num = Number(box.id.split('_')[2]);
            idList.push(num);
        }
    });

    if (idList.length === 0) return null;
    $.ajax({
        url:'/api/getIssueNonCashReceipt',
        method:'GET',
        data: {idList:idList},
        success:function(data){
            const insertPoint = document.getElementById('newCashReceiptPoint');
            insertPoint.innerHTML = '';

            const newTable = document.createElement('table');
            newTable.className = 'thine';
            const newTbody = document.createElement('tbody');

            let flag=true;
            data?.forEach(temp => {
                const newTr = document.createElement('tr');

                const newTd1 = document.createElement('td');
                if(flag) newTd1.style.borderTop = '3px solid #ECECEC';
                newTd1.textContent = '승인금액';

                const newTd2 = document.createElement('td');
                if(flag) newTd2.style.borderTop = '3px solid #ECECEC';
                const newDiv = document.createElement('div');
                newDiv.className = 'overlap-box';
                const newInput = document.createElement('input');
                newInput.id = 'newReceipt'+temp.id;
                newInput.type = 'text';
                newInput.className = 'autoComma overlap-input newCashReceipt';
                newInput.oninput = function(){ formatAutoCommaLimit(this, temp.paymentAmount-temp.totalPrice);}
                newInput.value = (temp.paymentAmount-temp.totalPrice+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                const newSpan = document.createElement('span');
                newSpan.className = 'overlap-span';
                newSpan.textContent = ('/'+temp.paymentAmount).replace(/\B(?=(\d{3})+(?!\d))/g, ',');

                newDiv.appendChild(newInput);
                newDiv.appendChild(newSpan);
                newTd2.appendChild(newDiv);
                newTr.appendChild(newTd1);
                newTr.appendChild(newTd2);
                newTbody.appendChild(newTr);

                flag=false;
            });

            const numberTr = document.createElement('tr');
            const numberTd1 = document.createElement('td');
            numberTd1.textContent = '발급번호';
            const numberTd2 = document.createElement('td');
            const numberInput = document.createElement('input');
            numberInput.type = 'number';
            numberInput.id = 'newReceiptNumber';
            numberInput.style.height = '30px';
            numberTd2.appendChild(numberInput);
            numberTr.appendChild(numberTd1);
            numberTr.appendChild(numberTd2);
            newTbody.appendChild(numberTr);

            const radioTr = document.createElement('tr');
            const radioTd1 = document.createElement('td');
            radioTd1.textContent = '발급종류';
            const radioTd2 = document.createElement('td');
            const radioIndividual = document.createElement('input');
            radioIndividual.type = 'radio';
            radioIndividual.name = 'receipt_receiptType';
            radioIndividual.id = 'receipt_individual';
            radioIndividual.checked = true;
            const labelIndividual = document.createElement('label');
            labelIndividual.htmlFor = 'receipt_individual';
            labelIndividual.textContent = '개인';
            labelIndividual.style.marginRight = '5px';
            const radioBusiness = document.createElement('input');
            radioBusiness.type = 'radio';
            radioBusiness.name = 'receipt_receiptType';
            radioBusiness.id = 'receipt_business';
            const labelBusiness = document.createElement('label');
            labelBusiness.htmlFor = 'receipt_business';
            labelBusiness.textContent = '사업자';
            labelBusiness.style.marginRight = '5px';
            radioTd2.appendChild(radioIndividual);
            radioTd2.appendChild(labelIndividual);
            radioTd2.appendChild(radioBusiness);
            radioTd2.appendChild(labelBusiness);
            radioTr.appendChild(radioTd1);
            radioTr.appendChild(radioTd2);
            newTbody.appendChild(radioTr);

            newTable.appendChild(newTbody);
            insertPoint.appendChild(newTable);
        },
        error: function(xhr) { console.log('error'); }
    });
    document.getElementById('saveCashReceiptModal').style.display = '';
}
function openCardReceiptModal(){
    const checkboxes = document.querySelectorAll('input[type="checkbox"].selectReceipt');
    checkboxes.forEach(box => box.checked=false);

    getCardReceipt(selectedRow5);
    document.getElementById('cardReceiptModal').style.display='';
}
function openCancelReceiptModal(){
    document.getElementById('cancelReceiptModal').style.display='';
}

// close Modal
function closeReceiptModal(){
    const rows = scrollArea5.querySelectorAll('table.history tbody tr');
    rows.forEach(temp => temp.style.backgroundColor = '');
    selectedRow5='';

    document.getElementById("modalItem").style.display = "block";
    const allModal = document.querySelectorAll('.receiptModal');
    allModal?.forEach(modal => modal.style.display='none');

    document.getElementById('cashReceiptPoint').innerHTML = '';
    document.getElementById('cardReceiptPoint').innerHTML = '';
}
function closeCancelReceiptModal(){ document.getElementById('cancelReceiptModal').style.display='none'; }

// receipt
function getReceipt(id){
    $.ajax({
        url:'/api/getReceipt',
        method:'GET',
        data: {id:id},
        success:function(data){
            if(data.status===false) document.querySelectorAll('.receiptStatus').forEach(btn => btn.disabled = true);
            else document.querySelectorAll('.receiptStatus').forEach(btn => btn.disabled = false);

            document.getElementById('orderModDate').textContent = formatDateTime(data.ordersDTO.modDate);
            document.getElementById('orderReceiptNumber').textContent = data.ordersDTO.receiptNumber;

            const count = (data!=='' && data!==null)? data.totalCount : 0;
            const discount = (data!=='' && data!==null)? data.ordersDTO.orderDiscountAmount+data.ordersDTO.totalDiscountAmount : 0;
            const price = (data!=='' && data!==null)? data.ordersDTO.orderAmount : 0;
            const payment = (data!=='' && data!==null)? data.ordersDTO.totalPaymentAmount : 0;
            const paymentPrice = (data!=='' && data!==null)? data.paymentPrice : 0;

            document.getElementById('receiptPrice').textContent = (price+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
            document.getElementById('receiptDiscount').textContent = (discount+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
            document.getElementById('receiptPayment').textContent = (price-discount+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
            document.getElementById('receiptReceived').textContent = (paymentPrice+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
            document.getElementById('receiptChange').textContent = (paymentPrice-price+discount+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');

            document.getElementById('cancelReceiptPrice').textContent = (price-discount+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원';

            let rowList = (data!=='' && data!==null)?data.rowList : null;

            let insertPoint = document.getElementById('receiptBodyPoint');
            insertPoint.innerHTML = '';

            let num = 1;
            rowList?.forEach(row => {
                const newTr = document.createElement('tr');

                const newName = document.createElement('td');
                const newNameDiv = document.createElement('div');
                newName.style.width = '25%';
                if(row.rowType==='item' || row.rowType==='allDiscount') {
                    newName.classList.add('product');
                    newNameDiv.textContent = num+'.';
                    num++;
                }else newName.style.paddingLeft = '20px';
                newNameDiv.className = 'ellipsis';
                newNameDiv.textContent += row.name;
                newName.appendChild(newNameDiv);
                newTr.appendChild(newName);

                const newPerPrice = document.createElement('td');
                if(row.rowType==='item' || row.rowType==='allDiscount') newPerPrice.classList.add('product');
                if(row.perPrice !== null) {
                    if(row.rowType==='discount' || row.rowType==='allDiscount') newPerPrice.textContent = '-'+row.perPrice;
                    else newPerPrice.textContent = row.perPrice;
                }
                newTr.appendChild(newPerPrice);

                const newCount = document.createElement('td');
                newCount.className = 'row-center';
                if(row.rowType==='item' || row.rowType==='allDiscount') newCount.classList.add('product');
                newCount.textContent = row.count;
                newTr.appendChild(newCount);

                const totalPrice = document.createElement('td');
                if(row.rowType==='item' || row.rowType==='allDiscount') totalPrice.classList.add('product');
                if(row.totalPrice !== null) totalPrice.textContent = (row.totalPrice+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                if(row.discountPrice !== null) totalPrice.textContent = row.discountPrice;
                newTr.appendChild(totalPrice);

                insertPoint.appendChild(newTr);
            });
        },
        error: function(xhr) { console.log('error'); }
    });
}
// cashReceipt
function getCashReceipt(id){
    $.ajax({
        url:'/api/getCashReceipt',
        method:'GET',
        data: {id:id},
        success:function(data){
            const issueList = data.issueList;
            const cancelList = data.cancelList;

            const cashReceiptPoint = document.getElementById('cashReceiptPoint');
            cashReceiptPoint.innerHTML = '';

            issueList?.forEach(temp => {
                const newDiv = document.createElement('div');
                newDiv.className = 'receipt-checkBoxTable';

                const newCheckDiv = document.createElement('div');
                newCheckDiv.style.height = '30px';
                newCheckDiv.style.width = '30px';
                const newCheckBox = document.createElement('input');
                newCheckBox.type = 'checkbox';
                newCheckBox.id = 'check_issue_'+temp.id;
                newCheckBox.className = 'selectReceipt';
                newCheckDiv.appendChild(newCheckBox);

                const newTableDiv = document.createElement('div');
                newTableDiv.style.flex = 1;

                const newTable = document.createElement('table');
                newTable.className = 'thine';
                const newTbody = document.createElement('tbody');

                const priceTr = document.createElement('tr');
                priceTr.style.cursor = 'pointer';
                priceTr.onclick = function(e) { openReceipt(e); };
                const priceTd1 = document.createElement('td');
                priceTd1.style.width = '100px';
                priceTd1.style.borderTop = '3px solid #ECECEC';
                priceTd1.textContent = '승인금액';
                const priceTd2 = document.createElement('td');
                priceTd2.style.borderTop = '3px solid #ECECEC';
                priceTd2.textContent = (temp.totalAmount+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원';
                priceTr.appendChild(priceTd1);
                priceTr.appendChild(priceTd2);
                newTbody.appendChild(priceTr);

                const dateTr = document.createElement('tr');
                dateTr.className ='hidden-row';
                dateTr.style.display = 'none';
                const dateTd1 = document.createElement('td');
                dateTd1.textContent = '매출일자';
                const dateTd2 = document.createElement('td');
                dateTd2.textContent = formatDateTime(temp.regDate);
                dateTr.appendChild(dateTd1);
                dateTr.appendChild(dateTd2);
                newTbody.appendChild(dateTr);

                const receiptTr = document.createElement('tr');
                receiptTr.className ='hidden-row';
                receiptTr.style.display = 'none';
                const receiptTd1 = document.createElement('td');
                receiptTd1.textContent = '영수증번호';
                const receiptTd2 = document.createElement('td');
                receiptTd2.textContent = temp.receiptNumber;
                receiptTr.appendChild(receiptTd1);
                receiptTr.appendChild(receiptTd2);
                newTbody.appendChild(receiptTr);

                const numberTr = document.createElement('tr');
                numberTr.className ='hidden-row';
                numberTr.style.display = 'none';
                const numberTd1 = document.createElement('td');
                numberTd1.textContent = '발급번호';
                const numberTd2 = document.createElement('td');
                numberTd2.textContent = temp.cashReceiptNumber;
                numberTr.appendChild(numberTd1);
                numberTr.appendChild(numberTd2);
                newTbody.appendChild(numberTr);

                const typeTr = document.createElement('tr');
                typeTr.className ='hidden-row';
                typeTr.style.display = 'none';
                const typeTd1 = document.createElement('td');
                typeTd1.textContent = '발급종류';
                const typeTd2 = document.createElement('td');
                typeTd2.textContent = (temp.cashReceiptType)?'사업자':'개인';
                typeTr.appendChild(typeTd1);
                typeTr.appendChild(typeTd2);
                newTbody.appendChild(typeTr);

                newTable.appendChild(newTbody);
                newTableDiv.appendChild(newTable);

                newDiv.appendChild(newCheckDiv);
                newDiv.appendChild(newTableDiv);

                cashReceiptPoint.appendChild(newDiv);
            });
            cancelList?.forEach(temp => {
                const newDiv = document.createElement('div');
                newDiv.className = 'receipt-checkBoxTable';

                const newCheckDiv = document.createElement('div');
                newCheckDiv.style.height = '30px';
                newCheckDiv.style.width = '30px';
                const newCheckBox = document.createElement('input');
                newCheckBox.type = 'checkbox';
                newCheckBox.id = 'check_cancel_'+temp.id;
                newCheckBox.className = 'selectReceipt';
                newCheckDiv.appendChild(newCheckBox);

                const newTableDiv = document.createElement('div');
                newTableDiv.style.flex = 1;

                const newTable = document.createElement('table');
                newTable.className = 'thine';
                const newTbody = document.createElement('tbody');

                const priceTr = document.createElement('tr');
                priceTr.style.cursor = 'pointer';
                priceTr.onclick = function(e) { openReceipt(e); };
                const priceTd1 = document.createElement('td');
                priceTd1.style.width = '100px';
                priceTd1.style.borderTop = '3px solid #ECECEC';
                priceTd1.textContent = '승인금액';
                const priceTd2 = document.createElement('td');
                priceTd2.style.borderTop = '3px solid #ECECEC';
                const priceSpan1 = document.createElement('span');
                priceSpan1.textContent = (temp.totalAmount+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원';
                const priceSpan2 = document.createElement('span');
                priceSpan2.style.color = 'red';
                priceSpan2.textContent = '(결제취소)';
                priceTd2.appendChild(priceSpan1);
                priceTd2.appendChild(priceSpan2);
                priceTr.appendChild(priceTd1);
                priceTr.appendChild(priceTd2);
                newTbody.appendChild(priceTr);

                const dateTr = document.createElement('tr');
                dateTr.className ='hidden-row';
                dateTr.style.display = 'none';
                const dateTd1 = document.createElement('td');
                dateTd1.textContent = '매출일자';
                const dateTd2 = document.createElement('td');
                dateTd2.textContent = formatDateTime(temp.regDate);
                dateTr.appendChild(dateTd1);
                dateTr.appendChild(dateTd2);
                newTbody.appendChild(dateTr);

                const dateTr2 = document.createElement('tr');
                dateTr2.className ='hidden-row';
                dateTr2.style.display = 'none';
                const dateTd3 = document.createElement('td');
                dateTd3.textContent = '취소일자';
                const dateTd4 = document.createElement('td');
                dateTd4.textContent = formatDateTime(temp.modDate);
                dateTr2.appendChild(dateTd3);
                dateTr2.appendChild(dateTd4);
                newTbody.appendChild(dateTr2);

                const receiptTr = document.createElement('tr');
                receiptTr.className ='hidden-row';
                receiptTr.style.display = 'none';
                const receiptTd1 = document.createElement('td');
                receiptTd1.textContent = '영수증번호';
                const receiptTd2 = document.createElement('td');
                receiptTd2.textContent = temp.receiptNumber;
                receiptTr.appendChild(receiptTd1);
                receiptTr.appendChild(receiptTd2);
                newTbody.appendChild(receiptTr);

                const numberTr = document.createElement('tr');
                numberTr.className ='hidden-row';
                numberTr.style.display = 'none';
                const numberTd1 = document.createElement('td');
                numberTd1.textContent = '발급번호';
                const numberTd2 = document.createElement('td');
                numberTd2.textContent = temp.cashReceiptNumber;
                numberTr.appendChild(numberTd1);
                numberTr.appendChild(numberTd2);
                newTbody.appendChild(numberTr);

                const typeTr = document.createElement('tr');
                typeTr.className ='hidden-row';
                typeTr.style.display = 'none';
                const typeTd1 = document.createElement('td');
                typeTd1.textContent = '발급종류';
                const typeTd2 = document.createElement('td');
                typeTd2.textContent = (temp.cashReceiptType)?'사업자':'개인';
                typeTr.appendChild(typeTd1);
                typeTr.appendChild(typeTd2);
                newTbody.appendChild(typeTr);

                newTable.appendChild(newTbody);
                newTableDiv.appendChild(newTable);

                newDiv.appendChild(newCheckDiv);
                newDiv.appendChild(newTableDiv);

                cashReceiptPoint.appendChild(newDiv);
            });
        },
        error: function(xhr) { console.log('error'); }
    });
}
function getNonCashReceipt(id){
    $.ajax({
        url:'/api/getNonCashReceipt',
        method:'GET',
        data: {id:id},
        success:function(data){
            const cashReceiptPoint = document.getElementById('cashReceiptPoint');
            cashReceiptPoint.innerHTML = '';

            if(data==='') return null;
            data?.forEach(temp => {
                const newDiv = document.createElement('div');
                newDiv.className = 'receipt-checkBoxTable';

                const newCheckDiv = document.createElement('div');
                newCheckDiv.style.height = '30px';
                newCheckDiv.style.width = '30px';
                const newCheckBox = document.createElement('input');
                newCheckBox.type = 'checkbox';
                newCheckBox.id = 'check_nonissue_'+temp.id;
                newCheckBox.className = 'selectReceipt';
                newCheckDiv.appendChild(newCheckBox);

                const newTableDiv = document.createElement('div');
                newTableDiv.style.flex = 1;

                const newTable = document.createElement('table');
                newTable.className = 'thine';
                const newTbody = document.createElement('tbody');

                const priceTr = document.createElement('tr');
                priceTr.style.cursor = 'pointer';
                priceTr.onclick = function(e) { openReceipt(e); };
                const priceTd1 = document.createElement('td');
                priceTd1.style.width = '100px';
                priceTd1.style.borderTop = '3px solid #ECECEC';
                priceTd1.textContent = '발행금액';
                const priceTd2 = document.createElement('td');
                priceTd2.style.borderTop = '3px solid #ECECEC';
                priceTd2.textContent = (temp.totalPrice+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원 / '
                                        +(temp.paymentAmount+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원';
                priceTr.appendChild(priceTd1);
                priceTr.appendChild(priceTd2);
                newTbody.appendChild(priceTr);

                const dateTr = document.createElement('tr');
                dateTr.className ='hidden-row';
                dateTr.style.display = 'none';
                const dateTd1 = document.createElement('td');
                dateTd1.textContent = '매출일자';
                const dateTd2 = document.createElement('td');
                dateTd2.textContent = formatDateTime(temp.regDate);
                dateTr.appendChild(dateTd1);
                dateTr.appendChild(dateTd2);
                newTbody.appendChild(dateTr);

                newTable.appendChild(newTbody);
                newTableDiv.appendChild(newTable);

                newDiv.appendChild(newCheckDiv);
                newDiv.appendChild(newTableDiv);

                cashReceiptPoint.appendChild(newDiv);
            });
        },
        error: function(xhr) { console.log('error'); }
    });
}
function getCardReceipt(id){
    $.ajax({
        url:'/api/getCardReceipt',
        method:'GET',
        data: {id:id},
        success:function(data){
            const issueList = data.issueList;
            const cancelList = data.cancelList;

            const cardReceiptPoint = document.getElementById('cardReceiptPoint');
            cardReceiptPoint.innerHTML = '';

            issueList?.forEach(temp => {
                const newDiv = document.createElement('div');
                newDiv.className = 'receipt-checkBoxTable';

                const newCheckDiv = document.createElement('div');
                newCheckDiv.style.height = '30px';
                newCheckDiv.style.width = '30px';
                const newCheckBox = document.createElement('input');
                newCheckBox.type = 'checkbox';
                newCheckBox.id = 'check_issue_'+temp.id;
                newCheckBox.className = 'selectReceipt';
                newCheckDiv.appendChild(newCheckBox);

                const newTableDiv = document.createElement('div');
                newTableDiv.style.flex = 1;

                const newTable = document.createElement('table');
                newTable.className = 'thine';
                const newTbody = document.createElement('tbody');

                const priceTr = document.createElement('tr');
                priceTr.style.cursor = 'pointer';
                priceTr.onclick = function(e) { openReceipt(e); };
                const priceTd1 = document.createElement('td');
                priceTd1.style.width = '100px';
                priceTd1.style.borderTop = '3px solid #ECECEC';
                priceTd1.textContent = '승인금액';
                const priceTd2 = document.createElement('td');
                priceTd2.style.borderTop = '3px solid #ECECEC';
                priceTd2.textContent = (temp.totalAmount+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원';
                priceTr.appendChild(priceTd1);
                priceTr.appendChild(priceTd2);
                newTbody.appendChild(priceTr);

                const dateTr = document.createElement('tr');
                dateTr.className ='hidden-row';
                dateTr.style.display = 'none';
                const dateTd1 = document.createElement('td');
                dateTd1.textContent = '매출일자';
                const dateTd2 = document.createElement('td');
                dateTd2.textContent = formatDateTime(temp.regDate);
                dateTr.appendChild(dateTd1);
                dateTr.appendChild(dateTd2);
                newTbody.appendChild(dateTr);

                const receiptTr = document.createElement('tr');
                receiptTr.className ='hidden-row';
                receiptTr.style.display = 'none';
                const receiptTd1 = document.createElement('td');
                receiptTd1.textContent = '영수증번호';
                const receiptTd2 = document.createElement('td');
                receiptTd2.textContent = temp.receiptNumber;
                receiptTr.appendChild(receiptTd1);
                receiptTr.appendChild(receiptTd2);
                newTbody.appendChild(receiptTr);

                const numberTr = document.createElement('tr');
                numberTr.className ='hidden-row';
                numberTr.style.display = 'none';
                const numberTd1 = document.createElement('td');
                numberTd1.textContent = '카드번호';
                const numberTd2 = document.createElement('td');
                numberTd2.textContent = temp.cardNumber+'('+temp.cardCompany+')';
                numberTr.appendChild(numberTd1);
                numberTr.appendChild(numberTd2);
                newTbody.appendChild(numberTr);

                const typeTr = document.createElement('tr');
                typeTr.className ='hidden-row';
                typeTr.style.display = 'none';
                const typeTd1 = document.createElement('td');
                typeTd1.textContent = '할부';
                const typeTd2 = document.createElement('td');
                typeTd2.textContent = (temp.cardMonth===0)?'일시불':temp.cardMonth+'개월';
                typeTr.appendChild(typeTd1);
                typeTr.appendChild(typeTd2);
                newTbody.appendChild(typeTr);

                newTable.appendChild(newTbody);
                newTableDiv.appendChild(newTable);

                newDiv.appendChild(newCheckDiv);
                newDiv.appendChild(newTableDiv);

                cardReceiptPoint.appendChild(newDiv);
            });
            cancelList?.forEach(temp => {
                const newDiv = document.createElement('div');
                newDiv.className = 'receipt-checkBoxTable';

                const newCheckDiv = document.createElement('div');
                newCheckDiv.style.height = '30px';
                newCheckDiv.style.width = '30px';
                const newCheckBox = document.createElement('input');
                newCheckBox.type = 'checkbox';
                newCheckBox.id = 'check_cancel_'+temp.id;
                newCheckBox.className = 'selectReceipt';
                newCheckDiv.appendChild(newCheckBox);

                const newTableDiv = document.createElement('div');
                newTableDiv.style.flex = 1;

                const newTable = document.createElement('table');
                newTable.className = 'thine';
                const newTbody = document.createElement('tbody');

                const priceTr = document.createElement('tr');
                priceTr.style.cursor = 'pointer';
                priceTr.onclick = function(e) { openReceipt(e); };
                const priceTd1 = document.createElement('td');
                priceTd1.style.width = '100px';
                priceTd1.style.borderTop = '3px solid #ECECEC';
                priceTd1.textContent = '승인금액';
                const priceTd2 = document.createElement('td');
                priceTd2.style.borderTop = '3px solid #ECECEC';
                const priceSpan1 = document.createElement('span');
                priceSpan1.textContent = (temp.totalAmount+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원';
                const priceSpan2 = document.createElement('span');
                priceSpan2.style.color = 'red';
                priceSpan2.textContent = '(결제취소)';
                priceTd2.appendChild(priceSpan1);
                priceTd2.appendChild(priceSpan2);
                priceTr.appendChild(priceTd1);
                priceTr.appendChild(priceTd2);
                newTbody.appendChild(priceTr);

                const dateTr = document.createElement('tr');
                dateTr.className ='hidden-row';
                dateTr.style.display = 'none';
                const dateTd1 = document.createElement('td');
                dateTd1.textContent = '매출일자';
                const dateTd2 = document.createElement('td');
                dateTd2.textContent = formatDateTime(temp.regDate);
                dateTr.appendChild(dateTd1);
                dateTr.appendChild(dateTd2);
                newTbody.appendChild(dateTr);

                const dateTr2 = document.createElement('tr');
                dateTr2.className ='hidden-row';
                dateTr2.style.display = 'none';
                const dateTd3 = document.createElement('td');
                dateTd3.textContent = '취소일자';
                const dateTd4 = document.createElement('td');
                dateTd4.textContent = formatDateTime(temp.modDate);
                dateTr2.appendChild(dateTd3);
                dateTr2.appendChild(dateTd4);
                newTbody.appendChild(dateTr2);

                const receiptTr = document.createElement('tr');
                receiptTr.className ='hidden-row';
                receiptTr.style.display = 'none';
                const receiptTd1 = document.createElement('td');
                receiptTd1.textContent = '영수증번호';
                const receiptTd2 = document.createElement('td');
                receiptTd2.textContent = temp.receiptNumber;
                receiptTr.appendChild(receiptTd1);
                receiptTr.appendChild(receiptTd2);
                newTbody.appendChild(receiptTr);

                const numberTr = document.createElement('tr');
                numberTr.className ='hidden-row';
                numberTr.style.display = 'none';
                const numberTd1 = document.createElement('td');
                numberTd1.textContent = '카드번호';
                const numberTd2 = document.createElement('td');
                numberTd2.textContent = temp.cardNumber+'('+temp.cardCompany+')';
                numberTr.appendChild(numberTd1);
                numberTr.appendChild(numberTd2);
                newTbody.appendChild(numberTr);

                const typeTr = document.createElement('tr');
                typeTr.className ='hidden-row';
                typeTr.style.display = 'none';
                const typeTd1 = document.createElement('td');
                typeTd1.textContent = '할부';
                const typeTd2 = document.createElement('td');
                typeTd2.textContent = (temp.cardMonth===0)?'일시불':temp.cardMonth+'개월';
                typeTr.appendChild(typeTd1);
                typeTr.appendChild(typeTd2);
                newTbody.appendChild(typeTr);

                newTable.appendChild(newTbody);
                newTableDiv.appendChild(newTable);

                newDiv.appendChild(newCheckDiv);
                newDiv.appendChild(newTableDiv);

                cardReceiptPoint.appendChild(newDiv);
            });
        },
        error: function(xhr) { console.log('error'); }
    });
}

// save
function saveNewCashReceipt(){
    let totalPrice = 0;
    const receiptMap = new Map();
    const inputs = document.querySelectorAll('.newCashReceipt');
    inputs.forEach(input => {
        const id = input.id.slice(10);
        const price = input.value.replace(/[^0-9]/g, '');
        totalPrice+=Number(price);
        receiptMap.set(id+'', Number(price));
    });

    const receiptNumber = document.getElementById('newReceiptNumber').value;
    const receiptType = document.getElementById('receipt_business').checked;

    $.ajax({
        url:'/api/saveNewCashReceipt',
        method:'POST',
        contentType: "application/json",
        data: JSON.stringify({receiptMap:Object.fromEntries(receiptMap), receiptNumber:receiptNumber,
                            receiptType:receiptType, totalPrice:totalPrice}),
        success:function(data){ openReceiptModal(); },
        error: function(xhr) { console.log('error'); }
    });
}

// delete
function cancelCashReceipt(){
    const idList = [];
    const checkboxes = document.querySelectorAll('input[type="checkbox"].selectReceipt');
    checkboxes.forEach(box => {
        if(box.checked === true){
            const num = Number(box.id.split('_')[2]);
            idList.push(num);
        }
    });

    $.ajax({
        url:'/api/deleteCashReceipt',
        method:'DELETE',
        data: {idList:idList},
        success:function(data){ openCashReceiptModal(); },
        error: function(xhr) { console.log('error'); }
    });
}
function cancelReceipt(){
    $.ajax({
        url:'/api/deleteReceipt',
        method:'DELETE',
        data: {id:selectedRow5},
        success:function(data){
            closeReceiptModal();
            search();
        },
        error: function(xhr) { console.log('error'); }
    });
}

// open folding tr
function openReceipt(e){
    const receiptTable = e.currentTarget.closest('table');
    receiptTable.querySelectorAll('.hidden-row').forEach(row => {
        if(row.style.display==='none') row.style.display = '';
        else row.style.display = 'none';
    });
}

// print
function printCashReceipt(){
    const idList = [];
    const checkboxes = document.querySelectorAll('input[type="checkbox"].selectReceipt');
    checkboxes.forEach(box => {
        if(box.checked === true){
            const num = Number(box.id.split('_')[2]);
            idList.push(num);
        }
    });
    $.ajax({
        url:'/api/printCashReceipt',
        method:'POST',
        data: {idList:idList},
        success:function(data){ },
        error: function(xhr) { console.log('error'); }
    });
}
function printCardReceipt(){
    const idList = [];
    const checkboxes = document.querySelectorAll('input[type="checkbox"].selectReceipt');
    checkboxes.forEach(box => {
        if(box.checked === true){
            const num = Number(box.id.split('_')[2]);
            idList.push(num);
        }
    });
    $.ajax({
        url:'/api/printCardReceipt',
        method:'POST',
        data: {idList:idList},
        success:function(data){ },
        error: function(xhr) { console.log('error'); }
    });
}