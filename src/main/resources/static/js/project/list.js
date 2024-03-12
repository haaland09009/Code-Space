 // 현재 페이지의 URL에서 카테고리 부분을 추출
    const urlParams = new URLSearchParams(window.location.search);
    const category = urlParams.get('category');

    // 모든 메뉴 항목에서 'active' 클래스 제거
    document.querySelectorAll('.menu-link').forEach(function (element) {
        element.classList.remove('text-black');
    });

    // 현재 페이지에 해당하는 메뉴 항목에 'active' 클래스 추가
    if (category == null) {
        document.querySelector('.menu-link[href="/project"]').classList.add('text-black');
    } else if (category == 'pro') {
        document.querySelector('.menu-link[href="/project?category=pro"]').classList.add('text-black');
    } else if (category == 'study') {
        document.querySelector('.menu-link[href="/project?category=study"]').classList.add('text-black');
    }


       // 홈 메뉴
        document.querySelector('.menu-link[href="/project"]').addEventListener('mouseover', function() {
            this.classList.add('text-black');
        });
        document.querySelector('.menu-link[href="/project"]').addEventListener('mouseout', function() {
            if (category != null) {
                this.classList.remove('text-black');
            }
        });


        // 기술 메뉴
        document.querySelector('.menu-link[href="/project?category=pro"]').addEventListener('mouseover', function() {
            this.classList.add('text-black');
        });
        document.querySelector('.menu-link[href="/project?category=pro"]').addEventListener('mouseout', function() {
            if (category != 'pro') {
                    this.classList.remove('text-black');
                }
        });

        // 커리어 메뉴
        document.querySelector('.menu-link[href="/project?category=study"]').addEventListener('mouseover', function() {
            this.classList.add('text-black');

        });
        document.querySelector('.menu-link[href="/project?category=study"]').addEventListener('mouseout', function() {
            if (category != 'study') {
                    this.classList.remove('text-black');
                }
        });


