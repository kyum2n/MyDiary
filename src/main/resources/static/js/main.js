

document.addEventListener('DOMContentLoaded', () => {
    const grid = document.querySelector('.grid');
    const loadMoreBtn = document.getElementById('load-more');
    const modalPage = document.getElementById('modal-page');
    const modalOverlay = document.getElementById('modal-otherpage');
    const closeModalBtn = document.getElementById('close-modal');
    const modalImage = document.getElementById('modal-image');
    const modalTitle = document.getElementById('modal-title');
    const modalDescription = document.getElementById('modal-description');

    let page = 0;
    const itemsPerPage = 10;
    let allData = [];

    const msnry = new Masonry(grid, {
        itemSelector: '.grid-item',
        columnWidth: '.grid-sizer',
        gutter: '.gutter-sizer',
        percentPosition: true
    });

    imagesLoaded(grid).on('progress', () => msnry.layout());

    // 모달 닫기
    function closeModal() {
        modalPage.style.display = 'none';
    }

    modalOverlay.addEventListener('click', closeModal);
    closeModalBtn.addEventListener('click', closeModal);

    // 모달 이벤트 등록 함수
    function addClickEvent(img, title, description) {
        img.addEventListener('click', () => {
            modalImage.src = img.src;
            modalTitle.textContent = title;
            modalDescription.textContent = description;
            modalPage.style.display = 'block';
        });
    }

    // 이미지 로드 후 교체하는 함수
    function renderItems(items) {
        items.forEach((entry, index) => {
            // 1. placeholder 스켈레톤 추가
            const placeholder = document.createElement('div');
            placeholder.className = 'grid-item placeholder';

            // ✅ 여기서 크기 부여는 placeholder에!
            // placeholder.style.width = getRandomInt(220, 300) + 'px';
            placeholder.style.height = getRandomInt(180, 240) + 'px';

            // ✅ spinner는 크기 고정된 정중앙 요소
            const spinner = document.createElement('div');
            spinner.className = 'loading-spinner';

            placeholder.appendChild(spinner);
            grid.appendChild(placeholder);
            msnry.appended(placeholder);

            // 2. 이미지 로드 후 교체
            const img = new Image();
            img.src = entry.image;
            img.alt = entry.title;

            img.onload = function () {
                const item = document.createElement('div');
                item.className = 'grid-item';
                item.appendChild(img);

                addClickEvent(img, entry.title, entry.description);

                grid.replaceChild(item, placeholder); // 교체
                imagesLoaded(item, () => {
                    msnry.appended(item);
                    msnry.layout();
                });
            };
        });
    }


    // 더보기 버튼 클릭 시
    loadMoreBtn.addEventListener('click', () => {
        page++;
        const start = (page - 1) * itemsPerPage;
        const end = start + itemsPerPage;
        const nextItems = allData.slice(start, end);
        renderItems(nextItems);

        if (end >= allData.length) {
            loadMoreBtn.style.display = 'none';
            document.getElementById('end-message').style.display = 'block';
        }
    });

    // JSON 데이터 로딩
    fetch('../static/data/dummydiary.json')
        .then(res => res.json())
        .then(data => {
            allData = data;
            loadMoreBtn.click(); // 첫 페이지 자동 로딩
        })
        .catch(err => console.error('JSON 로딩 실패:', err));

    function getRandomInt(min, max) {
        return Math.floor(Math.random() * (max - min + 1)) + min;
    }
});
