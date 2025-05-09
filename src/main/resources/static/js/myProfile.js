// 사용자 프로필 사진 변경
document.getElementById('changeImage').addEventListener('click', function () {
    document.getElementById('imageInput').click();
});

document.getElementById('imageInput').addEventListener('change', function () {
    if (this.files.length > 0) {
        document.getElementById('uploadNewUImage').submit();
    }
});

// 사용자 비밀번호 변경
function validatePwdChange() {
    let currentPwd = document.getElementById("currentPwd").value;
    let newPwd = document.getElementById("newPwd").value;
    let confirmPwd = document.getElementById("confirmPwd").value;
    let newPwdMsg = document.getElementById("newPwdMsg");
    let confirmPwdMsg = document.getElementById("confirmPwdMsg");
    let valid = true;
    if (!/^(?=.*[!@#$%^&*()_+=-]).{8,15}$/.test(newPwd)) {
        newPwdMsg.textContent = "새 비밀번호는 8~15자이며 특수문자를 포함해야 합니다.";
        newPwdMsg.className = "error";
        valid = false;
    } else if (currentPwd === newPwd) {
        newPwdMsg.textContent = "기존 비밀번호와 같을 수 없습니다.";
        newPwdMsg.className = "error";
        valid = false;
    } else {
        newPwdMsg.textContent = "사용 가능한 새 비밀번호입니다.";
        newPwdMsg.className = "success";
    }
    if (newPwd !== confirmPwd || confirmPwd === "") {
        confirmPwdMsg.textContent = "새 비밀번호가 일치하지 않습니다.";
        confirmPwdMsg.className = "error";
        valid = false;
    } else {
        confirmPwdMsg.textContent = "비밀번호가 일치합니다.";
        confirmPwdMsg.className = "success";
    }
    return valid;
}