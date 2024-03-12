
   // 홈 메뉴
   if (path == '/qna') {
       document.querySelector('.menu-item1').classList.add('text-black');
   }

   // 기술 메뉴
   else if (path == '/qna/tech') {
       document.querySelector('.menu-item2').classList.add('text-black');
   }

   // 커리어 메뉴
   else if (path == '/qna/career') {
       document.querySelector('.menu-item3').classList.add('text-black');
   }

   // 답변하기 메뉴
   else if (path == '/qna/answer') {
       document.querySelector('.menu-item4').classList.add('text-black');
   }



   // 홈 메뉴
    document.querySelector('.menu-item1').addEventListener('mouseover', function() {
        this.classList.add('text-black');
    });
    document.querySelector('.menu-item1').addEventListener('mouseout', function() {
        if (path != '/qna') {
            this.classList.remove('text-black');
        }
    });


    // 기술 메뉴
    document.querySelector('.menu-item2').addEventListener('mouseover', function() {
        this.classList.add('text-black');
    });
    document.querySelector('.menu-item2').addEventListener('mouseout', function() {
        if (path != '/qna/tech') {
            this.classList.remove('text-black');
        }
    });

    // 커리어 메뉴
    document.querySelector('.menu-item3').addEventListener('mouseover', function() {
        this.classList.add('text-black');

    });
    document.querySelector('.menu-item3').addEventListener('mouseout', function() {
        if (path != '/qna/career') {
            this.classList.remove('text-black');
        }
    });

    // 답변하기 메뉴
    document.querySelector('.menu-item4').addEventListener('mouseover', function() {
        this.classList.add('text-black');
    });
    document.querySelector('.menu-item4').addEventListener('mouseout', function() {
        if (path != '/qna/answer') {
            this.classList.remove('text-black');
        }
    });
