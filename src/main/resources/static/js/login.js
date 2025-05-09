function getCsrfToken() {
    return {
        headerName: document.getElementById("csrfHeader").value,
        token: document.getElementById("csrfToken").value
    };
}

function submitFindId() {
    const email = document.getElementById("findIdEmail").value;
    const csrf = getCsrfToken();

    fetch("/findId", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
            [csrf.headerName]: csrf.token
        },
        body: "email=" + encodeURIComponent(email)
    })
    .then(res => {
        if (!res.ok) throw new Error(res.status);
        return res.text();
    })
    .then(data => {
        document.getElementById("findIdResult").innerText = data;
    })
    .catch(err => {
        document.getElementById("findIdResult").innerText = "에러 발생: " + err;
    });
}

function submitFindPwd() {
    const uId = document.getElementById("findPwdUId").value;
    const csrf = getCsrfToken();

    fetch("/findPwd", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
            [csrf.headerName]: csrf.token
        },
        body: "uId=" + encodeURIComponent(uId)
    })
    .then(res => {
        if (!res.ok) throw new Error(res.status);
        return res.text();
    })
    .then(data => {
        document.getElementById("findPwdResult").innerText = data;
    })
    .catch(err => {
        document.getElementById("findPwdResult").innerText = "에러 발생: " + err;
    });
}

function closeModal(id) {
    const modal = document.getElementById(id);
    if (modal) {
        modal.style.display = 'none';
    }
}