let indexList=[];
let spaceList=[];

let selectedProduct;
let selectedProductCount = 0;
let tempDiscountPrice = 0;
let basicPrice = 0;
let waiting = 1;

document.addEventListener("DOMContentLoaded", () => {
    focusOutProductCount();
    clickRow2();
    clickRow3();
    clickRow4();
    showScrollArea2();
    showScrollArea3();
    showScrollArea4();
});

// page가져오기
function getPage(){
    $.ajax({
        url:'/api/getOrderPage',
        method:'GET',
        data: {waiting:waiting},
        success:function(data){
            scrollStep = scrollArea.clientHeight-60;
            size = Math.floor((scrollArea.offsetHeight-35)/30)*2;
            showList(1, data);
        },
        error: function(xhr) { console.log('error'); }
    });
    closeOrderModal();
}
function getPage2(){
    $.ajax({
        url:'/api/getOrderPage',
        method:'GET',
        data: {waiting:waiting},
        success:function(data){
            scrollStep2 = scrollArea.clientHeight-60;
            size2 = Math.floor((scrollArea2.offsetHeight-35)/30)*2;
            showList(2, data);
        },
        error: function(xhr) { console.log('error'); }
    });
    closeOrderModal();
}
function getPage3(){
    $.ajax({
        url:'/api/getOrderPage',
        method:'GET',
        data: {waiting:4},
        success:function(data){
            scrollStep3 = scrollArea3.clientHeight-60;
            size3 = Math.floor((scrollArea3.offsetHeight-35)/30)*2;
            showList(3, data);
        },
        error: function(xhr) { console.log('error'); }
    });
    closeOrderModal();
}
function getPage4(){
    $.ajax({
        url:'/api/getOrderPage',
        method:'GET',
        data: {waiting:4},
        success:function(data){
            scrollStep4 = scrollArea4.clientHeight-60;
            size4 = Math.floor((scrollArea4.offsetHeight-35)/30)*2;
            showList(4, data);
        },
        error: function(xhr) { console.log('error'); }
    });
    closeOrderModal();
}
function showList(type, data){
    const count = (data!=='' && data!==null)? data.totalCount : 0;
    const discount = (data!=='' && data!==null)? data.ordersDTO.orderDiscountAmount+data.ordersDTO.totalDiscountAmount : 0;
    const price = (data!=='' && data!==null)? data.ordersDTO.orderAmount : 0;
    const payment = (data!=='' && data!==null)? data.ordersDTO.totalPaymentAmount : 0;
    const paymentPrice = (data!=='' && data!==null)? data.paymentPrice : 0;

    const tempType = (type===2)? 2:(type===3)?3: (type===4)?4:'';
    document.getElementById('totalCount'+tempType).textContent = (count+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'개';
    document.getElementById('totalDiscount'+tempType).textContent = (discount+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    document.getElementById('totalPrice'+tempType).textContent = (price+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    document.getElementById('totalPayment'+tempType).textContent = (payment+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원';

    if(type!==2){
        document.getElementById('sumPrice').textContent = (price+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        document.getElementById('sumDiscount').textContent = (discount+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        document.getElementById('sumNonPayment').textContent = (price-discount+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        document.getElementById('sumPayment').textContent = (paymentPrice+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        document.getElementById('sumChange').textContent = (paymentPrice-price+discount+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');

        document.getElementById('nonPaymentPrice').textContent = (Math.abs(paymentPrice-price+discount)+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원';
        document.getElementById('paymentCashPrice').value = (Math.abs(paymentPrice-price+discount)+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        document.getElementById('paymentCardPrice').value = (Math.abs(paymentPrice-price+discount)+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        document.getElementById('paymentCashChange').textContent = '0원';
    }

    let rowList = (data!=='' && data!==null)?data.rowList : null;

    let insertPoint = (type===2)?addInsertPoint : (type===3)?thirdInsertPoint : (type===4)?fourthInsertPoint : defaultInsertPoint;
    insertPoint.innerHTML = '';

    let num = 1;
    rowList?.forEach(row => {
        const newTr = document.createElement('tr');
        newTr.id = row.rowType+'_'+row.mainId+'_'+row.id;

        if(type===2 && row.id===Number(selectedRow2)){
            document.getElementById('allDownBtn').disabled = false;
            document.getElementById('oneDownBtn').disabled = false;
        }
        if(type===4 && row.id===Number(selectedRow4)){
            document.getElementById('allUpBtn').disabled = false;
            document.getElementById('oneUpBtn').disabled = false;
        }

        if(row.id===Number(selectedRow) || row.id===Number(selectedRow2) || row.id===Number(selectedRow4)) newTr.style.backgroundColor = '#81ACEC';

        const newNum = document.createElement('td');
        if(row.rowType==='item' || row.rowType==='allDiscount') {
            newNum.classList.add('product');
            newNum.textContent = num;
            num++;
        }
        newTr.appendChild(newNum);

        const newName = document.createElement('td');
        newName.style.width = '25%';
        if(row.rowType==='item' || row.rowType==='allDiscount') newName.classList.add('product');
        const newNameDiv = document.createElement('div');
        newNameDiv.className = 'ellipsis';
        newNameDiv.textContent = row.name;
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
        newCount.style.gap = '0px';
        if(row.rowType==='item'){
            if(type===1){
                const newMinusBtn = document.createElement('button');
                newMinusBtn.className = 'input-number';
                newMinusBtn.textContent = '－';
                newMinusBtn.onclick = () => changeProductCount(row.id, false);
                newCount.appendChild(newMinusBtn);
                const newInput = document.createElement('input');
                newInput.id = 'mainInput'+row.id;
                newInput.type ='number';
                newInput.className = 'minOne';
                newInput.value = row.count;
                newInput.min = '1';
                newCount.appendChild(newInput);
                const newPlusBtn = document.createElement('button');
                newPlusBtn.className = 'input-number';
                newPlusBtn.textContent = '＋';
                newPlusBtn.onclick = () => changeProductCount(row.id, true);
                newCount.appendChild(newPlusBtn);
            }else newCount.textContent = row.count;
        }else if(row.count !== null) newCount.textContent = row.count;
        newTr.appendChild(newCount);

        const discountPrice = document.createElement('td');
        if(row.rowType==='item' || row.rowType==='allDiscount') discountPrice.classList.add('product');
        if(row.discountPrice !== null) discountPrice.textContent = row.discountPrice;
        newTr.appendChild(discountPrice);

        const totalPrice = document.createElement('td');
        if(row.rowType==='item' || row.rowType==='allDiscount') totalPrice.classList.add('product');
        if(row.totalPrice !== null) totalPrice.textContent = (row.totalPrice+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        newTr.appendChild(totalPrice);

        insertPoint.appendChild(newTr);
    });

    if(type===1) updateButtonColors();
    else if(type===2) updateButtonColors2();
    else if(type===3) {
        updateButtonColors3();
        getSeparateListContent();
    }
    else if(type===4) {
        updateButtonColors4();
        getSeparateListContent();
    }

    getTruePaymentHistory(type);
}

// 수량 변경
function focusOutProductCount(){
    scrollArea.addEventListener('focusout', function(e) {
        if (e.target.tagName === 'INPUT') {
            const id = Number(e.target.id.slice(9));
            const count = Number(e.target.value);
            $.ajax({
                url:'/api/updateOrderItemCount',
                method:'PUT',
                contentType: "application/json",
                data: JSON.stringify({waiting:waiting, id:id, count:e.target.value}),
                success:function(data){
                    getPage();
                    getPaymentHistory(1);
                },
                error: function(xhr) {
                    let response = JSON.parse(xhr.responseText);
                    if (response.errors) alert(response.errors.join("\n"));
                    else alert("알 수 없는 에러가 발생했습니다.");
                }
            });
        }
    });
}
function changeProductCount(id, type){
    let count = document.getElementById('mainInput'+id).value;

    if(type) count = Number(count)+1;
    else count = Number(count)-1;

    $.ajax({
        url:'/api/updateOrderItemCount',
        method:'PUT',
        contentType: "application/json",
        data: JSON.stringify({waiting:waiting, id:id, count:count}),
        success:function(data){
            getPage();
            getPaymentHistory(1);
        },
        error: function(xhr) {
            let response = JSON.parse(xhr.responseText);
            if (response.errors) alert(response.errors.join("\n"));
            else alert("알 수 없는 에러가 발생했습니다.");
        }
    });
}
function changeWaiting(num){
    waiting = num;

    const allBtn = document.querySelectorAll("button[id^='waiting']")
    allBtn.forEach(btn => {
        if(Number(btn.id.slice(7))===num) btn.classList.add('now');
        else btn.classList.remove('now');
    });

    getPage();
}

// clickRow
function clickRow(){
    scrollArea.addEventListener("click", (event) => {
        const rows = scrollArea.querySelectorAll('table.order tbody tr');
        const row = event.target.closest('tbody tr');

        if(row!==null) {
            const color = row.style.backgroundColor;
            if(color==='' && event.target.tagName !== 'BUTTON' && event.target.tagName !== 'INPUT') {
                rows.forEach(temp => temp.style.backgroundColor = '');
                row.style.backgroundColor = '#81ACEC';
                selectedRowDetail = row.id;
                if(row.id.slice(0, 4)==='item') {
                    selectedRow = row.id.split('_')[2];
                    selectedRow2 = row.id.split('_')[2];
                }
                else if(row.id.slice(0, 6)==='option' || row.id.slice(0, 8)==='discount') selectedRow = row.id.split('_')[1];
                else selectedRow = '';
            }else{
                row.style.backgroundColor = '';
                selectedRowDetail= '';
                selectedRow= '';
            }
        }
    });
}
function clickRow2(){
    scrollArea2.addEventListener("click", (event) => {
        const rows = scrollArea2.querySelectorAll('table.order tbody tr');
        const row = event.target.closest('tbody tr');

        if(row!==null) {
            const color = row.style.backgroundColor;
            if(color==='' && event.target.tagName !== 'BUTTON' && event.target.tagName !== 'INPUT') {
                rows.forEach(temp => temp.style.backgroundColor = '');
                if(row.id.slice(0, 4)==='item'){
                    row.style.backgroundColor = '#81ACEC';
                    selectedRow = row.id.split('_')[2];
                    selectedRow2 = row.id.split('_')[2];
                    document.getElementById('allDownBtn').disabled = false;
                    document.getElementById('oneDownBtn').disabled = false;
                }else {
                    selectedRow2 = '';
                    document.getElementById('allDownBtn').disabled = true;
                    document.getElementById('oneDownBtn').disabled = true;
                }
            }else{
                row.style.backgroundColor = '';
                selectedRow2= '';
                document.getElementById('allDownBtn').disabled = true;
                document.getElementById('oneDownBtn').disabled = true;
            }
        }
    });
}
function clickRow3(){
    scrollArea3.addEventListener("click", (event) => {
        const rows = scrollArea4.querySelectorAll('table.order tbody tr');
        const row = event.target.closest('tbody tr');

        if(row!==null) {
            const color = row.style.backgroundColor;
            if(color==='' && event.target.tagName !== 'BUTTON' && event.target.tagName !== 'INPUT') {
                rows.forEach(temp => temp.style.backgroundColor = '');
                if(row.id.slice(0, 4)==='item'){
                    row.style.backgroundColor = '#81ACEC';
                    selectedRow3 = row.id.split('_')[2];
                    selectedRow4 = row.id.split('_')[2];
                    document.getElementById('allUpBtn').disabled = false;
                    document.getElementById('oneUpBtn').disabled = false;
                }else {
                    selectedRow3 = '';
                    selectedRow4 = '';
                    document.getElementById('allUpBtn').disabled = true;
                    document.getElementById('oneUpBtn').disabled = true;
                }
            }else{
                row.style.backgroundColor = '';
                selectedRow3= '';
                selectedRow4 = '';
                document.getElementById('allUpBtn').disabled = true;
                document.getElementById('oneUpBtn').disabled = true;
            }
        }
    });
}
function clickRow4(){
    scrollArea4.addEventListener("click", (event) => {
        const rows = scrollArea4.querySelectorAll('table.order tbody tr');
        const row = event.target.closest('tbody tr');

        if(row!==null) {
            const color = row.style.backgroundColor;
            if(color==='' && event.target.tagName !== 'BUTTON' && event.target.tagName !== 'INPUT') {
                rows.forEach(temp => temp.style.backgroundColor = '');
                if(row.id.slice(0, 4)==='item'){
                    row.style.backgroundColor = '#81ACEC';
                    selectedRow3 = row.id.split('_')[2];
                    selectedRow4 = row.id.split('_')[2];
                    document.getElementById('allUpBtn').disabled = false;
                    document.getElementById('oneUpBtn').disabled = false;
                }else {
                    selectedRow3 = '';
                    selectedRow4 = '';
                    document.getElementById('allUpBtn').disabled = true;
                    document.getElementById('oneUpBtn').disabled = true;
                }
            }else{
                row.style.backgroundColor = '';
                selectedRow3 = '';
                selectedRow4= '';
                document.getElementById('allUpBtn').disabled = true;
                document.getElementById('oneUpBtn').disabled = true;
            }
        }
    });
}
// click menuBtn
function clickMenu(id){
    const rows = scrollArea.querySelectorAll('table.order tbody tr');
    rows.forEach(temp => temp.style.backgroundColor = '');
    selectedRow= '';

    const btn = document.getElementById('btn'+id);
    openOptionModal(btn, 'option');
}
// click upDownBtn
function clickMoveItem(type){
    let tempRow = selectedRow2;
    if(type===1 || type===2) tempRow = selectedRow4;

    document.getElementById('allUpBtn').disabled = true;
    document.getElementById('oneUpBtn').disabled = true;
    document.getElementById('allDownBtn').disabled = true;
    document.getElementById('oneDownBtn').disabled = true;

    $.ajax({
        url:'/api/moveOrderItem',
        method:'POST',
        contentType: "application/json",
        data: JSON.stringify({waiting:waiting, id:tempRow, type:type}),
        success:function(data){
            getPage2();
            getPage4();

            getPaymentHistory(2);
        },
        error: function(xhr) {
            let response = JSON.parse(xhr.responseText);
            if (response.errors) alert(response.errors.join("\n"));
            else alert("알 수 없는 에러가 발생했습니다.");
        }
    });
}

// delete order
function cancelOrder(type){
    if(type==='allCancel'){
        $.ajax({
            url:'/api/deleteOrders',
            method:'DELETE',
            data: {waiting:waiting},
            success:function(data){ getPage(); },
            error:function(xhr) { console.log('error'); }
        });
    }else{
        $.ajax({
            url:'/api/deleteOrderItem',
            method:'DELETE',
            data: {id:selectedRowDetail},
            success:function(data){ getPage(); },
            error:function(xhr) { console.log('error'); }
        });
    }
}

// menuScreen content
function openMenu(type){
    const menu = document.getElementById('menu');
    const menu_payment = document.getElementById('menu_payment');
    const cash_receipt = document.getElementById('cash_receipt');
    const search_member = document.getElementById('search_member');
    const search_receipt = document.getElementById('search_receipt');

    [menu, menu_payment, cash_receipt, search_member, search_receipt].forEach(temp => temp.style.display='none');
    document.querySelectorAll('.box-underMenu button').forEach(btn => btn.classList.remove('selected'));
    document.querySelectorAll('.paymentBtn').forEach(btn => btn.style.display = 'none');

    if(type==='menu'){
        getPage();
        [defaultPage, addPage, thirdPage, fourthPage].forEach(temp => temp.style.display='none');
        defaultPage.style.display='';
        menu.style.display='';
    }else if(type==='search_member'){
        search_member.style.display='';
        document.getElementById('search_member_btn').classList.add('selected');
    }else if(type==='search_receipt'){
        resetReceiptPage();
        search_receipt.style.display='';
        scrollStep5 = scrollArea5.clientHeight-60;
        size5 = Math.floor((scrollArea5.offsetHeight-35)/30)*2;
        document.getElementById('search_receipt_btn').classList.add('selected');
        getPage5();
    }else if(type==='cash_receipt'){
        const inputPrice = document.getElementById('paymentCashPrice').value;

        document.getElementById('cashReceiptPrice').textContent = inputPrice+'원';
        document.getElementById('receiptIssuePrice').value = inputPrice;

        if(paymentType.includes('separate')) document.getElementById('close_toSeparate2').style.display='';
        else if(paymentType.includes('mixed')) document.getElementById('close_toMixed2').style.display='';
        else document.getElementById('close_toMenu2').style.display='';

        cash_receipt.style.display='';
    }else{
        const calculateDisplay = document.getElementById('calculateDisplay');
        if(calculateDisplay.value!==null && calculateDisplay.value!=='0') {
            document.getElementById('paymentCashPrice').value = calculateDisplay.value;
            document.getElementById('paymentCardPrice').value = calculateDisplay.value;
            changePaymentCashPrice();
            changePaymentCardPrice();
            calculateDisplay.value = '0';
        }else if(document.getElementById('paymentCashPrice').value==='0') paymentAllPrice();

        document.querySelectorAll('.menuClose').forEach(btn => btn.style.display='none');
        document.querySelectorAll('.menuContent').forEach(cnt => cnt.style.display='none');
        document.querySelectorAll('.paymentInput').forEach(input => input.style.display='none');
        menu_payment.style.display='';

        const menu_payment_title = document.getElementById('menu_payment_title');
        if(type==='payment_cash' || type==='payment_card' || type==='payment_separate' || type==='payment_mixed'){
            [defaultPage, addPage, thirdPage, fourthPage].forEach(temp => temp.style.display='none');
            paymentType = type.split('_')[1];

            if(paymentType==='card') document.getElementById('paymentCardPrice').style.display='';
            else document.getElementById('paymentCashPrice').style.display='';
            if(paymentType==='separate') {
                getPage2();
                getPage4();
                addPage.style.display='';
                fourthPage.style.display='';
            }else{
                getPage();
                defaultPage.style.display='';
            }

            if(paymentType==='cash'){
                menu_payment_title.textContent = '현금결제';
                document.getElementById('saveCashPaymentBtn').style.display='';
            }else if(paymentType==='card'){
                menu_payment_title.textContent = '카드결제';
                document.getElementById('saveCardPaymentBtn').style.display='';
            }else if(paymentType==='separate'){
                menu_payment_title.textContent = '개별결제';
                getPaymentHistory(2);
            }else if(paymentType==='mixed'){
                menu_payment_title.textContent = '복합결제';
                getPaymentHistory(1);
            }

            document.getElementById(type).style.display='';
            document.getElementById(type+'_btn').classList.add('selected');
            document.getElementById('close_toMenu').style.display='';
        }else{
            let typeName = '';
            const typeSplit1 = type.split('_')[1];
            const typeSplit2 = type.split('_')[2];

            if(typeSplit1==='separate') {
                typeName +='개별결제-';
                getPage3();
                [defaultPage, addPage, thirdPage, fourthPage].forEach(temp => temp.style.display='none');
                thirdPage.style.display='';
            }else typeName +='복합결제-';

            let upperText = typeSplit1.charAt(0).toUpperCase() + typeSplit1.slice(1).toLowerCase();
            document.getElementById('payment_'+typeSplit1+'_btn').classList.add('selected');
            document.getElementById('close_to'+upperText).style.display='';

            if(typeSplit2==='cash') typeName +='현금';
            else if(typeSplit2==='card') typeName +='카드';
            else typeName +='포인트';
            menu_payment_title.textContent = typeName;

            upperText = typeSplit2.charAt(0).toUpperCase() + typeSplit2.slice(1).toLowerCase();
            paymentType += typeSplit2;

            document.getElementById('payment'+upperText+'Price').style.display='';
            document.getElementById('save'+upperText+'PaymentBtn').style.display='';
            document.getElementById('payment_'+typeSplit2).style.display='';
        }
    }

    if(type==='payment_separate'){
        document.querySelector('.grid-underList').style.display='none';
        document.querySelector('.box-sum').style.display='none';
        document.querySelector('.box-calc').style.display='none';
        document.getElementById('separate_underList').style.display='';
        document.querySelector('.box-underList2').style.display='';
    }else{
        document.querySelector('.grid-underList').style.display='';
        document.querySelector('.box-sum').style.display='';
        document.querySelector('.box-calc').style.display='';
        document.getElementById('separate_underList').style.display='none';
        document.querySelector('.box-underList2').style.display='none';
    }
}

// drag&drop
function drag(ev) {}