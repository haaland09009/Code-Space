 // 현재 페이지의 URL에서 카테고리 부분을 추출
    const urlParams = new URLSearchParams(window.location.search);
    const category = urlParams.get('category');
    const order = urlParams.get('order');
    const status = urlParams.get('status');

    // 모든 메뉴 항목에서 'active' 클래스 제거
    document.querySelectorAll('.menu-link').forEach(function (element) {
        element.classList.remove('text-black');
        element.classList.remove('fw-semibold');
    });


    const pathname = window.location.pathname;
    if (pathname == '/project') {
        document.querySelector('.menu-link1').classList.add('text-black');
        document.querySelector('.menu-link1').classList.add('fw-semibold');
    } else if (pathname == '/project/category/pro') {
        document.querySelector('.menu-link2').classList.add('text-black');
        document.querySelector('.menu-link2').classList.add('fw-semibold');
    } else if (pathname == '/project/category/study') {
        document.querySelector('.menu-link3').classList.add('text-black');
        document.querySelector('.menu-link3').classList.add('fw-semibold');
    }


//    // 현재 페이지에 해당하는 메뉴 항목에 'active' 클래스 추가
//    if (category == null) {
//        document.querySelector('.menu-link[href="/project"]').classList.add('text-black');
//        document.querySelector('.menu-link[href="/project"]').classList.add('fw-semibold');
//    } else if (category == 'pro') {
//        document.querySelector('.menu-link[href="/project?category=pro"]').classList.add('text-black');
//        document.querySelector('.menu-link[href="/project?category=pro"]').classList.add('fw-semibold');
//    } else if (category == 'study') {
//        document.querySelector('.menu-link[href="/project?category=study"]').classList.add('text-black')
//        document.querySelector('.menu-link[href="/project?category=study"]').classList.add('fw-semibold');
//    }

    if (order == null) {
        document.querySelector('.sort-item1').classList.add('fw-bold');
        document.querySelector('.point-item1').classList.add('fw-bold');
        document.querySelector('.point-item1').classList.add('text-success');
        document.querySelector('.order-item1').classList.add('text-black');

    } else if (order == 'comment') {
        document.querySelector('.sort-item2').classList.add('fw-bold');
        document.querySelector('.point-item2').classList.add('fw-bold');
        document.querySelector('.point-item2').classList.add('text-success');
        document.querySelector('.order-item2').classList.add('text-black');

    } else if (order == 'clip') {
        document.querySelector('.sort-item3').classList.add('fw-bold');
        document.querySelector('.point-item3').classList.add('fw-bold');
        document.querySelector('.point-item3').classList.add('text-success');
        document.querySelector('.order-item3').classList.add('text-black');
    }

   if (status == null) {
        document.querySelector('.status-item1').classList.add('fw-bold');

    } else if (status == 'unrecruited') {
        document.querySelector('.status-item2').classList.add('fw-bold');

    } else if (status == 'recruited') {
        document.querySelector('.status-item3').classList.add('fw-bold');
    }


    document.querySelector('.menu-link1').addEventListener('mouseover', function() {
        this.classList.add('text-black');
    });
    document.querySelector('.menu-link1').addEventListener('mouseout', function() {
        if (pathname != '/project') {
            this.classList.remove('text-black');
        }
    });


    document.querySelector('.menu-link2').addEventListener('mouseover', function() {
        this.classList.add('text-black');
    });
    document.querySelector('.menu-link2').addEventListener('mouseout', function() {
        if (pathname != '/project/category/pro') {
                this.classList.remove('text-black');
            }
    });


    document.querySelector('.menu-link3').addEventListener('mouseover', function() {
        this.classList.add('text-black');

    });
    document.querySelector('.menu-link3').addEventListener('mouseout', function() {
        if (pathname != '/project/category/study') {
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


