let recentMember = null;

// 회원 조회
function getMember(type){
    let phoneNumber = null;
    phoneNumber = document.getElementById('memberInput'+type).value;
    $.ajax({
        url:'/api/getMemberByPhone',
        method:'GET',
        data: {phoneNumber:phoneNumber},
        success:function(data){
            let point = 0;
            if(data!==null && data!=='') point = data.points;
            recentMember = phoneNumber;

            if(data===null || data===''){
                document.getElementById('memberRecent'+type).style.display = 'none';
                document.getElementById('memberNewScreen'+type).style.display = '';
                document.getElementById('memberPointScreen'+type).style.display = 'none';
            }else if(data!==''){
                document.getElementById('memberRecent'+type).style.display = 'none';
                document.getElementById('memberNewScreen'+type).style.display = 'none';
                document.getElementById('memberPointScreen'+type).style.display = '';
                document.getElementById('memberPoint'+type).textContent = (point+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');

                if(type===2){
                    const paymentPointPrice = document.getElementById('paymentPointPrice');
                    paymentPointPrice.value = (point+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
                    changePaymentPointPrice();
                    document.getElementById('pointPaymentBtn').disabled = false;
                }
            }
        },
        error: function(xhr) { console.log('error'); }
    });
}
function searchRecentMember(type){
    if(recentMember===null) return alert('최근 조회한 회원이 존재하지 않습니다.');
    document.getElementById('memberInput'+type).value = recentMember;
    getMember(type);
}

// reset
function resetMemberSearch(type){
    document.getElementById('memberRecent'+type).style.display = '';
    document.getElementById('memberNewScreen'+type).style.display = 'none';
    document.getElementById('memberPointScreen'+type).style.display = 'none';
    if(type===2) {
        document.getElementById('memberInput2').disabled = false;
        document.getElementById('pointPaymentBtn').disabled = true;
    }
}

// save
function saveMember(type){
    let phoneNumber = document.getElementById('memberInput'+type).value;
    $.ajax({
        url:'/api/saveMember',
        method:'POST',
        contentType: "application/json",
        data: JSON.stringify({id:0, phoneNumber:phoneNumber, mail:true}),
        success:function(data){
            let point = 0;
            if(data!==null && data!=='') point = data.points;
            recentMember = phoneNumber;

            document.getElementById('memberRecent'+type).style.display = 'none';
            document.getElementById('memberNewScreen'+type).style.display = 'none';
            document.getElementById('memberPointScreen'+type).style.display = '';
            document.getElementById('memberPoint'+type).textContent = (point+'').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        },
        error: function(xhr) {
            let response = JSON.parse(xhr.responseText);
            if (response.errors) alert(response.errors.join("\n"));
            else alert("알 수 없는 에러가 발생했습니다.");
        }
    });
}