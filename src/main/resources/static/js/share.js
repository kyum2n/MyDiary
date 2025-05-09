// 현재 날짜 불러오기
document.addEventListener("DOMContentLoaded", function () {
    const today = new Date();
    const options = { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' };
    document.getElementById("date-now").textContent = today.toLocaleDateString("ko-KR", options);
});

// 로그아웃
function handleSocialLogout() {
    // 네이버 로그아웃 팝업
    const naverPopup = window.open("https://nid.naver.com/nidlogin.logout", "naverLogout", "width=1,height=1");
    
    setTimeout(() => {
        try { if (naverPopup) naverPopup.close(); } catch (e) {}
        
        const csrfToken = document.querySelector('input[name="_csrf"]').value;
        
        // 폼 생성하여 서버로 POST 전송
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = '/logout';

        const csrfInput = document.createElement('input');
        csrfInput.type = 'hidden';
        csrfInput.name = '_csrf';
        csrfInput.value = csrfToken;

        form.appendChild(csrfInput);
        document.body.appendChild(form);
        form.submit();
    }, 300);
}