// 일기 내용 입력 필드
tinymce.init({
    selector: '#content',
    height: 240,
    width: '100%',
    plugins: [
        'emoticons', 'image', 'link', 'lists',
        'media', 'table', 'wordcount', 'code', 'charmap'
    ],
    toolbar: 'undo redo | blocks fontfamily fontsize | bold italic underline strikethrough | link image media table | numlist bullist | emoticons charmap | removeformat | code',
    menubar: false,
    branding: false,
    setup: function(editor) {
        editor.on('change', function () {
            document.getElementById('content').value = editor.getContent();
        });
    }
});

// 이미지 미리보기
function previewImage(event) {
    const file = event.target.files[0];
    if (file) {
        const preview = document.getElementsByClassName('imagePreview')[0];
        if (preview){
            preview.src = URL.createObjectURL(file);
            preview.style.display = 'block';
        }
    }
}

// 입력받은 위치의 날씨 불러오기
function fetchWeather() {
    const input = document.getElementById("locationInput");
    const location = input.value;

    fetch(`/weather?location=${encodeURIComponent(location)}`)
        .then(response => response.text())
        .then(data => {
            document.getElementById("weatherPreview").innerText = data;
            document.getElementById("location").value = location;
            document.getElementById("weather").value = data;
        });
}
