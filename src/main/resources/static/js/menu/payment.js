let paymentType = 'menu';
let recentPaymentMethod = null;

// payment price
function paymentAllPrice(){
    document.getElementById('paymentCashPrice').value = document.getElementById('nonPaymentPrice').textContent.slice(0, -1);
    document.getElementById('paymentCardPrice').value = document.getElementById('nonPaymentPrice').textContent.slice(0, -1);
    document.getElementById('paymentPointPrice').value = document.getElementById('nonPaymentPrice').textContent.slice(0, -1);
    document.getElementById('paymentCashChange').textContent = '0원';
}
function changePaymentCashPrice(){
    const nonPayPrice = Number(document.getElementById('nonPaymentPrice').textContent.replace(/[^0-9]/g, ''));
    const inputPrice = Number(document.getElementById('paymentCashPrice').value.replace(/[^0-9]/g, ''));
    document.getElementById('paymentCashChange').textContent = (inputPrice - nonPayPrice + '').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원';
}
function changeCashReceiptPrice(){
    const paymentPrice = Number(document.getElementById('cashReceiptPrice').textContent.replace(/[^0-9]/g, ''));
    const inputPrice = Number(document.getElementById('receiptIssuePrice').value.replace(/[^0-9]/g, ''));
    if(paymentPrice<inputPrice) document.getElementById('receiptIssuePrice').value = document.getElementById('cashReceiptPrice').textContent.slice(0, -1);
}
function changePaymentCardPrice(){
    const nonPayPrice = Number(document.getElementById('nonPaymentPrice').textContent.replace(/[^0-9]/g, ''));
    const inputPrice = Number(document.getElementById('paymentCardPrice').value.replace(/[^0-9]/g, ''));
    if(nonPayPrice<inputPrice) document.getElementById('paymentCardPrice').value=document.getElementById('nonPaymentPrice').textContent.slice(0, -1);
}
function changePaymentPointPrice(){
    const nonPayPrice = Number(document.getElementById('nonPaymentPrice').textContent.replace(/[^0-9]/g, ''));
    const havingPoint = Number(document.getElementById('memberPoint2').textContent.replace(/[^0-9]/g, ''));
    const inputPrice = Number(document.getElementById('paymentPointPrice').value.replace(/[^0-9]/g, ''));
    if(inputPrice<nonPayPrice){
        if(havingPoint<inputPrice) document.getElementById('paymentPointPrice').value=document.getElementById('memberPoint2').textContent;
    }else{
        if(havingPoint<nonPayPrice) document.getElementById('paymentPointPrice').value=document.getElementById('memberPoint2').textContent;
        else document.getElementById('paymentPointPrice').value=document.getElementById('nonPaymentPrice').textContent.slice(0, -1);
    }
}

// reset paymentScreen
function resetPaymentScreen(type){
    document.getElementById('receiptIssuePrice').value = '';
    document.getElementById('individual').checked = true;
    document.getElementById('business').checked = false;
    document.getElementById('receiptNumber').value = '';
    document.getElementById('cardCompany').value = '';
    document.getElementById('cardNumber').value = '';
    document.getElementById('cardMonth').value = 0;
    document.getElementById('memberInput2').disabled = false;
    document.getElementById('pointPaymentBtn').disabled = true;
    if(type==='all'){
        memberInput2.value = '';
        memberInput2.dispatchEvent(new Event('input'));
    }
    document.getElementById('mixedInsertPoint').innerHTML = '';
    document.getElementById('separateInsertPoint').innerHTML = '';
}

// save payment
function savePayment(type){
    if(type=='receipt') saveCashReceipt(paymentType);
    else if(paymentType.includes('cash')) saveCashPayment(paymentType);
    else if(paymentType.includes('card')) saveCardPayment(paymentType);
    else if(paymentType.includes('point')) savePointPayment(paymentType);
}
function savePaymentResult(data, type){
    document.getElementById('allUpBtn').disabled = true;
    document.getElementById('oneUpBtn').disabled = true;
    document.getElementById('allDownBtn').disabled = true;
    document.getElementById('oneDownBtn').disabled = true;

    if(type.includes('separate')) getPage4();
    else getPage();
    recentPaymentMethod = null;

    const message=data.message;
    if(message==='Non'){
        alert('주문을 선택해 주세요.');
        openMenu('menu');
        resetPaymentScreen('all');
    }else if(message==='Finish') {
        openMenu('menu');
        resetPaymentScreen('all');
    }else if(message==='Point'){
        recentPaymentMethod = data.paymentMethodId;
        openMenu('menu');
        resetPaymentScreen('all');
        openMemberPointModal();
    }else{
        if(type.includes('separate')) openMenu('payment_separate');
        else if(type.includes('mixed')) openMenu('payment_mixed');
        else if(type.includes('card')) openMenu('payment_card');
        else openMenu('payment_cash');
        resetPaymentScreen('particle');
    }
}
function saveCashPayment(type){
    const tempAllPayment = Number(document.getElementById('sumNonPayment').textContent.replace(/[^0-9]/g, ''));
    const tempNonPayment = Number(document.getElementById('nonPaymentPrice').textContent.replace(/[^0-9]/g, ''));
    const tempReceived = Number(document.getElementById('paymentCashPrice').value.replace(/[^0-9]/g, ''));
    const tempWaiting = (type.includes('separate'))?4 : waiting;

    const received = tempReceived;
    if(received<10) return alert('결제금액은 10원 이상부터 가능합니다.');
    let price = tempReceived;
    let change = 0;
    if(tempNonPayment < tempReceived){
        price = tempNonPayment;
        change = tempReceived-tempNonPayment;
    }

    $.ajax({
        url:'/api/saveCashPayment',
        method:'POST',
        contentType: "application/json",
        data: JSON.stringify({waiting:tempWaiting, received:received, price:price, change:change}),
        success:function(data){ savePaymentResult(data, type); },
        error: function(xhr) {
            let response = JSON.parse(xhr.responseText);
            if (response.errors) alert(response.errors.join("\n"));
            else alert("알 수 없는 에러가 발생했습니다.");
        }
    });
}
function saveCashReceipt(type){
    const tempAllPayment = Number(document.getElementById('sumNonPayment').textContent.replace(/[^0-9]/g, ''));
    const tempNonPayment = Number(document.getElementById('nonPaymentPrice').textContent.replace(/[^0-9]/g, ''));
    const tempReceived = Number(document.getElementById('paymentCashPrice').value.replace(/[^0-9]/g, ''));
    const tempWaiting = (type.includes('separate'))?4 : waiting;

    const received = tempReceived;
    if(received<10) return alert('결제금액은 10원 이상부터 가능합니다.');
    let price = tempReceived;
    let change = 0;
    if(tempNonPayment < tempReceived){
        price = tempNonPayment;
        change = tempReceived-tempNonPayment;
    }
    const receiptIssuePrice = Number(document.getElementById('receiptIssuePrice').value.replace(/[^0-9]/g, ''));
    const receiptType = document.getElementById('business').checked;
    const receiptNumber = Number(document.getElementById('receiptNumber').value.replace(/[^0-9]/g, ''));

    $.ajax({
        url:'/api/saveCashReceipt',
        method:'POST',
        contentType: "application/json",
        data: JSON.stringify({waiting:tempWaiting, received:received, price:price, change:change, receiptIssuePrice:receiptIssuePrice, receiptType:receiptType, receiptNumber:receiptNumber}),
        success:function(data){ savePaymentResult(data, type); },
        error: function(xhr) {
            let response = JSON.parse(xhr.responseText);
            if (response.errors) alert(response.errors.join("\n"));
            else alert("알 수 없는 에러가 발생했습니다.");
        }
    });
}
function saveCardPayment(type){
    const received = Number(document.getElementById('paymentCardPrice').value.replace(/[^0-9]/g, ''));
    if(received<1) return alert('결제금액은 1원 이상부터 가능합니다.');
    const cardCompany = document.getElementById('cardCompany').value;
    const cardNumber = document.getElementById('cardNumber').value;
    const cardMonth = document.getElementById('cardMonth').value;
    const tempWaiting = (type.includes('separate'))?4 : waiting;

    $.ajax({
        url:'/api/saveCardPayment',
        method:'POST',
        contentType: "application/json",
        data: JSON.stringify({waiting:tempWaiting, received:received, cardCompany:cardCompany, cardNumber:cardNumber, cardMonth:cardMonth}),
        success:function(data){ savePaymentResult(data, type); },
        error: function(xhr) {
            let response = JSON.parse(xhr.responseText);
            if (response.errors) alert(response.errors.join("\n"));
            else alert("알 수 없는 에러가 발생했습니다.");
        }
    });
}
function savePointPayment(type){
    const received = Number(document.getElementById('paymentPointPrice').value.replace(/[^0-9]/g, ''));
    if(received<1) return alert('사용 포인트는 1 이상부터 가능합니다.');
    const phoneNumber = document.getElementById('memberInput2').value;
    const tempWaiting = (type.includes('separate'))?4 : waiting;

    $.ajax({
        url:'/api/savePointPayment',
        method:'POST',
        contentType: "application/json",
        data: JSON.stringify({waiting:tempWaiting, received:received, phoneNumber:phoneNumber}),
        success:function(data){ savePaymentResult(data, type); },
        error: function(xhr) {
            let response = JSON.parse(xhr.responseText);
            if (response.errors) alert(response.errors.join("\n"));
            else alert("알 수 없는 에러가 발생했습니다.");
        }
    });
}

// payment history
function getTruePaymentHistory(type){
    let tempWaiting = waiting;
    if(type===3 || type===4) tempWaiting=4;
    $.ajax({
        url:'/api/getTruePaymentHistory',
        method:'GET',
        data: {waiting:tempWaiting},
        success:function(data){
            if(type===1){
                if(data===0) document.querySelectorAll('.mixedEvent').forEach(point => point.style.pointerEvents = 'auto');
                else document.querySelectorAll('.mixedEvent').forEach(point => point.style.pointerEvents = 'none');
            }else if(type===3 || type===4){
                if(data===0) document.querySelectorAll('.separateEvent').forEach(point => point.style.pointerEvents = 'auto');
                else document.querySelectorAll('.separateEvent').forEach(point => point.style.pointerEvents = 'none');
            }
        },
        error:function(xhr) { console.log('error'); }
    });
}
function getPaymentHistory(type){
    const tempWaiting = (type===1)?waiting : 4;
    $.ajax({
        url:'/api/getPaymentHistory',
        method:'GET',
        data: {waiting:tempWaiting},
        success:function(data){
            let insertPoint = document.getElementById('mixedInsertPoint');
            if(type===2) insertPoint = document.getElementById('separateInsertPoint');
            insertPoint.innerHTML = '';

            if(data!==null && data!== ''){
                const member = data.memberDTO;
                const methodList = data.methodReceiptList;
                if(member!==null){
                    const memberInput2 = document.getElementById('memberInput2');
                    memberInput2.disabled = true;
                    memberInput2.value = member.phoneNumber;
                    getMember(2);
                }
                if(methodList!==null){
                    let number = 1;
                    for (const temp of methodList) {
                        const method = temp.methodDTO;
                        const receipt = temp.receiptDTO;

                        const newTr = document.createElement('tr');
                        newTr.id = 'method'+method.id;
                        newTr.onclick = () => openCancelMethodModal(method.id);

                        const newTemp = document.createElement('td');
                        newTemp.textContent = number+'';
                        newTr.appendChild(newTemp);
                        number++;

                        const newType = document.createElement('td');
                        newType.id = 'methodType'+method.id;
                        newType.textContent = (method.typeValue===0)?'현금':(method.typeValue===1)?'카드':'포인트';
                        newTr.appendChild(newType);

                        const newNumber = document.createElement('td');
                        if(receipt!==null) newNumber.textContent = (receipt.cardNumber!==null)? receipt.cardNumber:'';
                        newTr.appendChild(newNumber);

                        const newPrice = document.createElement('td');
                        newPrice.id = 'methodPrice'+method.id;
                        newPrice.textContent = (method.paymentAmount+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',')+'원';
                        newTr.appendChild(newPrice);

                        const newAuth = document.createElement('td');
                        if(receipt!==null) newAuth.textContent = (receipt.authorizationNumber!==null)? receipt.authorizationNumber:'';
                        newTr.appendChild(newAuth);

                        const newStatus = document.createElement('td');
                        newStatus.id = 'methodStatus'+method.id;
                        newStatus.textContent = (method.status===true)?'승인':'취소';
                        newTr.appendChild(newStatus);

                        insertPoint.appendChild(newTr);
                    }
                }
            }
        },
        error:function(xhr) { console.log('error'); }
    });
}
function getSeparateListContent(){
    $.ajax({
        url:'/api/getSeparateListContent',
        method:'GET',
        success:function(data){
            if(data===0) document.querySelectorAll('.separateListEvent').forEach(point => point.style.pointerEvents = 'auto');
            else document.querySelectorAll('.separateListEvent').forEach(point => point.style.pointerEvents = 'none');
        },
        error:function(xhr) { console.log('error'); }
    });
}