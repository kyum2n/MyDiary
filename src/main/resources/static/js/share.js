// 현재 날짜 불러오기
document.addEventListener("DOMContentLoaded", function () {
    const today = new Date();
    const options = { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' };
    document.getElementById("date-now").textContent = today.toLocaleDateString("ko-KR", options);
});

// 로그아웃
function handleSocialLogout() {
    const provider = sessionStorage.getItem("loginProvider");

    if (provider === "naver") {
        // 팝업은 반드시 이 시점에 바로 띄워야 브라우저가 허용함
        const naverPopup = window.open(
            "https://nid.naver.com/nidlogin.logout",
            "naverLogout",
            "width=400,height=300"
        );

        // 팝업 닫히는 걸 기다렸다가 로그아웃 처리
        const popupTimer = setInterval(() => {
            if (naverPopup && naverPopup.closed) {
                clearInterval(popupTimer);
                submitLogoutForm();
            }
        }, 300);
    } else {
        // 카카오, 일반 등은 바로 로그아웃
        submitLogoutForm();
    }
}

// 로그아웃 폼 전송
function submitLogoutForm() {
    const csrfToken = document.querySelector('input[name="_csrf"]').value;

    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/customLogout';

    const csrfInput = document.createElement('input');
    csrfInput.type = 'hidden';
    csrfInput.name = '_csrf';
    csrfInput.value = csrfToken;

    form.appendChild(csrfInput);
    document.body.appendChild(form);
    form.submit();
}