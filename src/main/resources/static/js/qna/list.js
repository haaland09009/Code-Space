    const pathname = window.location.pathname;
    if (pathname == '/qna') {
        document.querySelector('.menu-item1').classList.add('text-black');
    } else if (pathname == '/qna/topic/tech') {
        document.querySelector('.menu-item2').classList.add('text-black');
        document.querySelector('.icon-item2').classList.add('text-success');
        document.querySelector('.icon-item2').classList.add('fw-semibold');
    } else if (pathname == '/qna/topic/career') {
        document.querySelector('.menu-item3').classList.add('text-black');
        document.querySelector('.icon-item3').classList.add('text-primary');
        document.querySelector('.icon-item3').classList.add('fw-semibold');
    } else if (pathname == '/qna/no-questions') {
        document.querySelector('.menu-item4').classList.add('text-black');
        document.querySelector('.icon-item4').style.color = "#8b00ff";
    }


    // 현재 페이지의 URL에서 카테고리 부분을 추출
    const urlParams = new URLSearchParams(window.location.search);
    const sortKey = urlParams.get('sortKey');

    if (sortKey == 'reply' && searchWord == null) {
        document.querySelector(".sortPoint1").classList.add("text-success");
        document.querySelector(".sortName1").classList.add("text-black");
        document.querySelector(".sortName1").classList.add("fw-semibold");
    } else if (sortKey == null && searchWord == null) {
        if (pathname != '/qna/no-questions') {
            document.querySelector(".sortPoint").classList.add("text-success");
            document.querySelector(".sortName").classList.add("text-black");
            document.querySelector(".sortName").classList.add("fw-semibold");
        }
    }

   // 홈 메뉴
    document.querySelector('.menu-item1').addEventListener('mouseover', function() {
        this.classList.add('text-black');
    });
    document.querySelector('.menu-item1').addEventListener('mouseout', function() {
        if (pathname != '/qna') {
            this.classList.remove('text-black');
        }
    });

    // 기술 메뉴
    document.querySelector('.menu-item2').addEventListener('mouseover', function() {
        this.classList.add('text-black');
    });
    document.querySelector('.menu-item2').addEventListener('mouseout', function() {
        if (pathname != '/qna/topic/tech') {
            this.classList.remove('text-black');
        }
    });

    // 커리어 메뉴
    document.querySelector('.menu-item3').addEventListener('mouseover', function() {
        this.classList.add('text-black');

    });
    document.querySelector('.menu-item3').addEventListener('mouseout', function() {
        if (pathname != '/qna/topic/career') {
            this.classList.remove('text-black');
        }
    });

    // 답변하기 메뉴
    document.querySelector('.menu-item4').addEventListener('mouseover', function() {
        this.classList.add('text-black');
    });
    document.querySelector('.menu-item4').addEventListener('mouseout', function() {
        if (pathname != '/qna/no-questions') {
            this.classList.remove('text-black');
        }
    });

    /* 검색 */
    document.querySelector("#searchInput").addEventListener("keypress", function(event) {
      if (event.keyCode === 13) {
          event.preventDefault(); // 기본 제출 방지
          document.querySelector("#searchForm").submit(); // 폼 제출
      }
  });

