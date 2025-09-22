// open Modal
function openMemberPointModal(){ document.getElementById('memberPointModal').style.display=''; }
function openSavePointModal(){ document.getElementById('savePointModal').style.display=''; }
function openCancelMethodModal(id){
    const status = document.getElementById('methodStatus'+id).textContent;
    if(status==='승인'){
        document.getElementById('cancelModalType').textContent = document.getElementById('methodType'+id).textContent;
        document.getElementById('cancelModalPrice').textContent = document.getElementById('methodPrice'+id).textContent;
        document.getElementById('cancelMethodBtn').dataset.value = id;
        document.getElementById('cancelMethodModal').style.display='';
    }
}

// close Modal
function closePaymentModal(type){
    document.getElementById("modalItem").style.display = "block";
    const allModal = document.querySelectorAll('.paymentModal');
    allModal?.forEach(modal => modal.style.display='none');

    if(type===1) {
        recentPaymentMethod = null;
        openMenu('menu');
        resetPaymentScreen('all');
    }else if(type===2) openMemberPointModal();
}
function closeCancelMethodModal(){
    document.getElementById('cancelMethodModal').style.display='none';
}

// save point
function saveMemberPoint(){
    const phoneNumber = document.getElementById('memberInput3').value;

    $.ajax({
        url:'/api/saveMemberPoint',
        method:'POST',
        contentType: "application/json",
        data: JSON.stringify({id:recentPaymentMethod, phoneNumber:phoneNumber}),
        success:function(data){
            closePaymentModal(1);
            resetPaymentScreen('particle');
            const memberInput = document.getElementById('memberInput3');
            memberInput.value = '';
            memberInput.dispatchEvent(new Event('input'));
        },
        error: function(xhr) {
            const res = xhr.responseJSON;
            if (res && res.message) alert(res.message);
            else alert("알 수 없는 오류 발생");
        }
    });
}

// cancel
function cancelMethod(button){
    const id = button.dataset.value;
    $.ajax({
        url:'/api/deletePaymentMethod',
        method:'DELETE',
        data: {id:id},
        success:function(data){
            closeCancelMethodModal();
            resetPaymentScreen('particle');
            if(paymentType.includes('separate')) {
                getPaymentHistory(2);
                getPage3();
            }else {
                getPaymentHistory(1);
                getPage();
            }
        },
        error: function(xhr) {
            const res = xhr.responseJSON;
            if (res && res.message) alert(res.message);
            else alert("알 수 없는 오류 발생");
        }
    });
}