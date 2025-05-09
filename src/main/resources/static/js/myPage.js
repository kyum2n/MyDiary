
// 마이 페이지 모달 열기
document.addEventListener('DOMContentLoaded', () => {
    const grid = document.querySelector('.grid');
    const loadMoreBtn = document.getElementById('load-more');
    const modalPage = document.getElementById('modal-page');
    const modalOverlay = document.getElementById('modal-overlay');
    const closeModalBtn = document.getElementById('close-modal');
    const modalImage = document.getElementById('modal-image');
    const modalTitle = document.getElementById('modal-title');
    const modalContent = document.getElementById('modal-content');

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

    // 캘린더에서 클릭한 일기 상세보기
    document.querySelectorAll(".day-entry").forEach(entry => {
        entry.addEventListener("click", async function () {
            const date = this.dataset.date;

            try {
                const response = await fetch(`/api/diary/${date}`);
                if (!response.ok) throw new Error("데이터를 불러오지 못했습니다");

                const data = await response.json();

                const modalTitle = document.getElementById("modal-title");
                const modalContent = document.getElementById("modal-content");
                const modalImage = document.getElementById("modal-image");
                const modalDate = document.getElementById("modal-date");
                const modalBtn = document.getElementById("modal-btn");

                // 일기가 있으면
                if (data.title) {
                    modalTitle.textContent = data.title || "제목 없음";
                    modalContent.textContent = data.content || "내용 없음";
                    modalImage.src = data.imageUrl || "/image/welcome.png";
                    modalDate.textContent = date;

                    modalBtn.innerHTML = `<button class="modal-btn edit-btn" onclick="window.location.href='/diary/edit?id=${data.id}'">편집하기</button>`;
                }
                // 일기가 없으면
                else {
                    modalTitle.textContent = "새 일기 작성";
                    modalContent.textContent = "일기가 없습니다. 새 일기를 작성하세요.";
                    modalImage.src = "/image/welcome.png";
                    modalDate.textContent = date;

                    modalBtn.innerHTML = `<button class="modal-btn create-btn" onclick="window.location.href='/newDiary?date=${date}'">새 일기 작성</button>`;
                }

                // 모달 띄우기
                document.getElementById("modal-page").style.display = "block";
            } catch (error) {
                console.error(error);
                alert("일기를 불러오지 못했습니다.");
            }
    });
    
       // 모달 닫기
        document.getElementById("modal-overlay").addEventListener("click", () => {
            document.getElementById("modal-page").style.display = "none";
        });
        document.getElementById("close-modal").addEventListener("click", () => {
            document.getElementById("modal-page").style.display = "none";
        });
    });
    
    // 일기 내용 모달에서 상세보기
    document.querySelectorAll('.thumbnail-img').forEach(img => {
        img.addEventListener('click', function () {
    
            fetch(`/api/diary/${date}`)
                .then(response => response.json())
                .then(data => {
                    setModalContent(data); // 공통 함수로 모달 세팅
                })
                .catch(error => console.error('Error loading diary detail:', error));
        });
    });
    

    // 리스트에서 사용하는 모달 이벤트 등록 함수
    function addClickEvent(img, data) {
        img.dataset.date = data.date;
    
        img.addEventListener('click', () => {
            setModalContent(data);
        });
    }

    // 모달 호출 함수
    function setModalContent(data) {
        const modalDate = data.date || "";

        modalTitle.textContent = data.title || '제목 없음';
        modalContent.textContent = data.content || '내용 없음';
        modalImage.src = data.image || '/image/welcome.png';
        modalDate.textContent = data.date || '';

        const btn = document.getElementById('modal-btn');
        if (data.id) {
            btn.innerHTML = `<button class="modal-btn edit-btn" onclick="window.location.href='/diary/edit?id=${data.id}'">편집하기</button>`;
        } else {
            btn.innerHTML = `<button class="modal-btn create-btn" onclick="window.location.href='/newDiary?date=${data.date}'">새 일기 작성</button>`;
        }

        modalPage.style.display = 'block';
    }

    // 이미지 로드 후 교체하는 함수
    function renderItems(items) {
        items.forEach((entry, index) => {
            // 1. placeholder 스켈레톤 추가
            const placeholder = document.createElement('div');
            placeholder.className = 'grid-item placeholder';

            // 여기서 크기 부여는 placeholder에!
            // placeholder.style.width = getRandomInt(220, 300) + 'px';
            placeholder.style.height = getRandomInt(180, 240) + 'px';

            // spinner는 크기 고정된 정중앙 요소
            const spinner = document.createElement('div');
            spinner.className = 'loading-spinner';

            placeholder.appendChild(spinner);
            grid.appendChild(placeholder);
            msnry.appended(placeholder);

            // 이미지 로드 후 교체
            const img = new Image();
            img.src = entry.image;
            img.alt = entry.title;

            img.onload = function () {
                const item = document.createElement('div');
                item.className = 'grid-item';
                item.appendChild(img);

                addClickEvent(img, entry.title, entry.content);

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


    // 현재 아이디 고정
    const currentUserId = 'user1';

    // 작성자의 게시물만 불러오기
    // JSON 데이터 로딩
    fetch('/data/dummydiary.json')
        .then(res => res.json())
        .then(data => {
            const myPosts = data.filter(post => post.authorId === currentUserId);
            allData = myPosts;

            const today = new Date();
            currentYear = today.getFullYear();
            currentMonth = today.getMonth();

            loadMoreBtn.click(); // 목록용
            renderCalendar(myPosts, currentYear, currentMonth); // 캘린더용
            // renderMapMarkers(myPosts); // 지도용

            document.getElementById('prev-month').addEventListener('click', () => {
                currentMonth--;
                if (currentMonth < 0) {
                    currentMonth = 11;
                    currentYear--;
                }
                renderCalendar(myPosts, currentYear, currentMonth);
            });

            document.getElementById('next-month').addEventListener('click', () => {
                currentMonth++;
                if (currentMonth > 11) {
                    currentMonth = 0;
                    currentYear++;
                }
                renderCalendar(myPosts, currentYear, currentMonth);
            });
        })
        .catch(err => console.error('JSON 로딩 실패:', err));


    // 탭으로 달력과 지도 전환
    const tabButtons = [calendarBtn, mapBtn] = [
        document.getElementById('calendarB'),
        document.getElementById('mapB')
    ];
    const tabs = [calendarTab, mapTab] = [
        document.getElementById('calendar'),
        document.getElementById('map')
    ];

    tabButtons.forEach((btn, i) => {
        btn.addEventListener('click', () => {
            tabButtons.forEach(b => b.classList.remove('activemp'));
            tabs.forEach(t => t.classList.remove('activemp'));
            btn.classList.add('activemp');
            tabs[i].classList.add('activemp');
        });
    });

    // 캘린더 렌더링 함수
    function renderCalendar(posts, year, month) {
        const calendarBody = document.getElementById('calendar-body');
        calendarBody.innerHTML = ''; // 기존 내용 초기화

        // const todayStr = new Date().toISOString().split('T')[0];
        function getTodayStringLocal() {
            const today = new Date();
            const yyyy = today.getFullYear();
            const mm = String(today.getMonth() + 1).padStart(2, '0');
            const dd = String(today.getDate()).padStart(2, '0');
            return `${yyyy}-${mm}-${dd}`;
        }
        const todayStr = getTodayStringLocal(); // 'yyyy-MM-dd' 형식

        const firstDay = new Date(year, month, 1);
        const lastDate = new Date(year, month + 1, 0).getDate();
        const prevLastDate = new Date(year, month, 0).getDate();
        const startDay = firstDay.getDay(); // 0 (일요일) ~ 6 (토요일)

        const cells = [];

        // 앞에 빈칸 → 이전 달 날짜
        for (let i = startDay - 1; i >= 0; i--) {
            const date = new Date(year, month - 1, prevLastDate - i);
            cells.push({ date, dimmed: true });
        }

        // 현재 달 날짜
        for (let day = 1; day <= lastDate; day++) {
            const date = new Date(year, month, day);
            cells.push({ date, dimmed: false });
        }

        // 다음 달 날짜
        while (cells.length < 42) {
            const lastDate = cells[cells.length - 1].date;
            const next = new Date(lastDate);
            next.setDate(lastDate.getDate() + 1);
            cells.push({ date: next, dimmed: true });
        }

        // 셀 만들기
        for (let week = 0; week < 6; week++) {
            const tr = document.createElement('tr');

            for (let i = 0; i < 7; i++) {
                const { date, dimmed } = cells[week * 7 + i];
                const yyyy = date.getFullYear();
                const mm = String(date.getMonth() + 1).padStart(2, '0');
                const dd = String(date.getDate()).padStart(2, '0');
                const dateStr = `${yyyy}-${mm}-${dd}`;


                const td = document.createElement('td');
                td.textContent = date.getDate(); // 날짜 숫자만 표시
                td.dataset.date = dateStr;

                // 클릭 시 일기 상세보기 또는 새 일기 작성
                td.addEventListener('click', () => {
                    fetch(`/api/diary/${dateStr}`)
                        .then(response => {
                            if (response.ok) return response.json();
                            else throw new Error('No diary found');
                        })
                        .then(data => {
                            setModalContent({
                                title: data.title,
                                content: data.content,
                                image: data.imageUrl || '',
                            });
                        })
                        .catch(() => {
                            setModalContent({
                                title: "새 일기 작성",
                                content: "이 날짜에는 작성된 일기가 없습니다.",
                                image: null,
                                date: dateStr,
                                id: null
                            });
                
                        });
                });

                if (dimmed) td.classList.add('dimmed');
                if (dateStr === todayStr) { td.classList.add('today'); }

                tr.appendChild(td);
            }

            calendarBody.appendChild(tr);
        }

        //  제목 갱신
        document.getElementById('calendar-title').textContent = `${year}년 ${month + 1}월`;

        posts.forEach(post => {
            const dateStr = post.date; // 'yyyy-MM-dd'
            const calendarCell = document.querySelector(`[data-date="${dateStr}"]`);

            if (calendarCell) {
                calendarCell.style.backgroundImage = `url('${post.image}')`;
                calendarCell.style.backgroundSize = 'cover';
                calendarCell.style.backgroundPosition = 'center';
            }
        });
    }

    // 캘린더 시운전
    const now = new Date();
    let currentYear = now.getFullYear();
    let currentMonth = now.getMonth();


    // 일기쓰러가기
    document.getElementById('write-today').addEventListener('click', () => {
        const today = new Date();
        const yyyy = today.getFullYear();
        const mm = String(today.getMonth() + 1).padStart(2, '0');
        const dd = String(today.getDate()).padStart(2, '0');
        const todayStr = `${yyyy}-${mm}-${dd}`;

        window.location.href = `/newDiary?date=${todayStr}`;
    });




    // 지도 만들기
    function getRandomInt(min, max) {
        return Math.floor(Math.random() * (max - min + 1)) + min;
    }


    // 지도 초기화
    function initMap() {
        const mapOptions = {
            center: { lat: 37.5665, lng: 126.9780 }, // 서울 좌표
            zoom: 10,
        };

        const map = new google.maps.Map(document.getElementById("map"), mapOptions);

        // 예시 마커
        const marker = new google.maps.Marker({
            position: { lat: 37.5665, lng: 126.9780 },
            map: map,
            title: "서울",
        });
    }
    // 전역적으로 등록
    window.initMap = initMap;
});
