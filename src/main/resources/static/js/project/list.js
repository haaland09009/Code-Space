   // 현재 페이지의 URL에서 카테고리 부분을 추출
    const urlParams = new URLSearchParams(window.location.search);
    const category = urlParams.get('category');


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
        selectedCategoryId = 1;
    } else if (pathname == '/project/category/study') {
        document.querySelector('.menu-link3').classList.add('text-black');
        document.querySelector('.menu-link3').classList.add('fw-semibold');
        selectedCategoryId = 2;
    }


    // 현재 페이지에 해당하는 메뉴 항목에 'active' 클래스 추가
  /*  if (category == null) {
        document.querySelector('.menu-link[href="/project"]').classList.add('text-black');
        document.querySelector('.menu-link[href="/project"]').classList.add('fw-semibold');
    } else if (category == 'pro') {
        document.querySelector('.menu-link[href="/project?category=pro"]').classList.add('text-black');
        document.querySelector('.menu-link[href="/project?category=pro"]').classList.add('fw-semibold');
    } else if (category == 'study') {
        document.querySelector('.menu-link[href="/project?category=study"]').classList.add('text-black')
        document.querySelector('.menu-link[href="/project?category=study"]').classList.add('fw-semibold');
    }*/

//    if (order == null) {
//        document.querySelector('.sort-item1').classList.add('fw-bold');
//        document.querySelector('.point-item1').classList.add('fw-bold');
//        document.querySelector('.point-item1').classList.add('text-success');
//        document.querySelector('.order-item1').classList.add('text-black');
//
//    } else if (order == 'comment') {
//        document.querySelector('.sort-item2').classList.add('fw-bold');
//        document.querySelector('.point-item2').classList.add('fw-bold');
//        document.querySelector('.point-item2').classList.add('text-success');
//        document.querySelector('.order-item2').classList.add('text-black');
//
//    } else if (order == 'clip') {
//        document.querySelector('.sort-item3').classList.add('fw-bold');
//        document.querySelector('.point-item3').classList.add('fw-bold');
//        document.querySelector('.point-item3').classList.add('text-success');
//        document.querySelector('.order-item3').classList.add('text-black');
//    }
//
//   if (status == null) {
//        document.querySelector('.status-item1').classList.add('fw-bold');
//
//    } else if (status == 'unrecruited') {
//        document.querySelector('.status-item2').classList.add('fw-bold');
//
//    } else if (status == 'recruited') {
//        document.querySelector('.status-item3').classList.add('fw-bold');
//    }


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




    /* 검색
//    document.querySelector("#searchInput").addEventListener("keypress", function(event) {
//      if (event.keyCode === 13) {
//          event.preventDefault(); // 기본 제출 방지
//          document.querySelector("#searchForm").submit(); // 폼 제출
//      }
//  });

 /* 기술스택 선택 box */
 document.querySelector("#toggleTechBox").addEventListener("click", function() {
    const positionList = document.querySelector("#positionListBox");
    const techList = document.querySelector("#techListBox");
    if (techList.style.display == "none") {
        techList.style.display = "block";
        techList.style.position = "absolute";
        techList.style.width = "880px";
        positionList.style.display = "none";
    } else {
        techList.style.display = "none";
    }
  });

 /* 포지션 선택 box */
 document.querySelector("#togglePositionBox").addEventListener("click", function() {
    const positionList = document.querySelector("#positionListBox");
    const techList = document.querySelector("#techListBox");
    if (positionList.style.display == "none") {
        positionList.style.display = "block";
        positionList.style.position = "absolute";
        positionList.style.width = "230px";
        techList.style.display = "none";
    } else {
        positionList.style.display = "none";
    }
 });

  /* 모집중 선택 box */
  document.querySelector("#toggleStatusBox").addEventListener("click", function() {

     if (projectStatus == null) {
        projectStatus = '모집중';
     } else {
        projectStatus = null;
     }

     reloadProjectList();

     const toggleStatusBox = document.querySelector("#toggleStatusBox");
     const statusNameBox = document.querySelector("#statusNameBox");

     if (toggleStatusBox.classList.contains("checkbox-border")) {
        toggleStatusBox.classList.remove("checkbox-border");
        statusNameBox.classList.remove("checkbox-font");
     } else {
        toggleStatusBox.classList.add("checkbox-border");
        statusNameBox.classList.add("checkbox-font");
     }
  });


   /* 북마크 선택 box */
   document.querySelector("#toggleClipBox").addEventListener("click", function() {

       if (sessionId == 0) {
          location.href  = "/user/loginPage";
          return;
       }

        if (clipYn == null) {
          clipYn = 'yes';
        } else {
          clipYn = null;
        }

       const toggleClipBox = document.querySelector("#toggleClipBox");
       const clipNameBox = document.querySelector("#clipNameBox");
       const clipIconBox = document.querySelector("#clipIconBox");

       if (toggleClipBox.classList.contains("checkbox-border")) {
          toggleClipBox.classList.remove("checkbox-border");
          clipNameBox.classList.remove("checkbox-font");
          clipIconBox.classList.remove("bi-bookmark-fill");
          clipIconBox.classList.add("bi-bookmark");
          clipIconBox.classList.remove("clipColor");
       } else {
          toggleClipBox.classList.add("checkbox-border");
          clipNameBox.classList.add("checkbox-font");
          clipIconBox.classList.remove("bi-bookmark");
          clipIconBox.classList.add("bi-bookmark-fill");
          clipIconBox.classList.add("clipColor");
       }

       reloadProjectList();
    });



  /* 기술스택 추가, 삭제 여부 */
  const toggleTech = (id) => {
    const techButton = document.querySelector("#techButton_" + id);
    if (techButton) {
        removeTech(id);
    } else {
        insertTech(id);
    }
  }

  /* 기술스택 추가 */
  const insertTech = (id) => {
     techIdList.push(id);
     selectTechList(techIdList);
     reloadProjectList();
     /* 기술스택 ui 변경 */
     modifyTechBox();
  }

  /* 기술스택 ui 변경 */
  const modifyTechBox = () => {
      const toggleTechBox = document.querySelector("#toggleTechBox");
      const techNameBox = document.querySelector("#techNameBox");

      let techNameList = [];
      for (let i = 0; i < techIdList.length; i++) {
          const techName = document.querySelector("#tech_" + techIdList[i]).innerText;
          techNameList.push(techName);
      }
      const techNameListBox = techNameList.join(", "); // 모든 기술 스택의 이름을 쉼표로 구분하여 연결
      techNameBox.innerText = techNameListBox;

      toggleTechBox.classList.add("checkbox-border");
      techNameBox.classList.add("checkbox-font");

      modifyTechBar();
  }


  /* 언어바 상태 변경 */
  const modifyTechBar = () => {
     const languageBar = document.querySelectorAll(".languageBar");

     if (techIdList.length == 0) {
        for (let i = 0; i < languageBar.length; i++) {
            languageBar[i].classList.remove("transparent_logo");
            languageBar[i].classList.add("full_logo");
        }
     } else {
         for (let i = 0; i < languageBar.length; i++) {
            const techId = parseInt(languageBar[i].id.split('_')[1]);
            if (techIdList.includes(techId)) {
                languageBar[i].classList.remove("transparent_logo");
                languageBar[i].classList.add("full_logo");
            } else {
                languageBar[i].classList.remove("full_logo");
                languageBar[i].classList.add("transparent_logo");
            }
        }
    }
  }

  /* 기술스택 제거 */
  const removeTech = (id) => {
    for (let i = 0; i < techIdList.length; i++) {
        if (techIdList[i] == id) {
            techIdList.splice(i, 1);
        }
    }
    selectTechList(techIdList);

    reloadProjectList();

    if (techIdList.length == 0) {
       /* 기술스택 ui 초기화 */
       resetTechBox();
    } else {
       modifyTechBox();
    }
 }

 /* 기술스택 모두 제거 (초기화) */
 const removeTechAll = () => {
      techIdList = [];
      selectTechList(techIdList);
      reloadProjectList();

      /* 기술스택 ui 초기화 */
      resetTechBox();
  }


  /* 기술스택 ui 초기화 */
  const resetTechBox = () => {
       const toggleTechBox = document.querySelector("#toggleTechBox");
       const techNameBox = document.querySelector("#techNameBox");

       toggleTechBox.classList.remove("checkbox-border");
       techNameBox.classList.remove("checkbox-font");
       techNameBox.innerText = "기술 스택";

       modifyTechBar();
   }



  /* 선택한 기술스택 조회 */
  const selectTechList = (techIdList) => {
     $.ajax({
         type: "post",
         url: "/tech/list",
         data: {
            techIdList: techIdList.length > 0 ? techIdList : []
          },
         success: function(res) {
            let output = "";
            if (res.length == 0) {

            } else {
                for (let i in res) {
                    output += '<button class="btn btn-sm selectButton ms-1 me-2" id="techButton_' + res[i].id + '" onclick="removeTech(' + res[i].id + ')">';
                    output += '<span class="fs-18">' + res[i].name + '</span>';
                    output += '<span class="ms-2">';
                    output += '<img src="/img/delete.svg" style="width: 18px; height: 18px; position: relative; bottom: 2px">';
                    output += '</span>';
                    output += '</button>';
                }
                output += '<button class="btn text-secondary fw-semibold fs-18 ms-1 removeAllButton" onclick="removeTechAll()">';
                output += '<i class="bi bi-arrow-clockwise">' + '</i>';
                output += '<span class="ms-2">' + "초기화" + '</span>';
                output += '</button>';
            }
            document.querySelector("#selectedTechList").innerHTML = output;
         },
         error: function(err) {
             return;
         }
     });
  }

  /* 포지션 선택 */
  const selectPosition = (id) => {
       const positionListBox =  document.querySelector("#positionListBox");
       const togglePositionBox = document.querySelector("#togglePositionBox");
       const positionNameBox = document.querySelector("#positionNameBox");
       const positionName = document.querySelector("#position_" + id);

       positionListBox.style.display = "none";

       selectedPosId = id;
       reloadProjectList();


       togglePositionBox.classList.add("checkbox-border");
       positionNameBox.classList.add("checkbox-font");
       positionNameBox.innerText = positionName.innerText;
  }

 /* 게시물 목록 조회 */
  const reloadProjectList = () => {
    $.ajax({
       type: "get",
       url: "/project/list",
       data: {
           techIdList: techIdList.length > 0 ? techIdList : [],
           positionId: (selectedPosId == 0 || selectedPosId == null) ? null : selectedPosId,
           status: projectStatus != null ? projectStatus : null,
           categoryId: selectedCategoryId,
           clipYn: clipYn != null ? clipYn : null
       },
       success: function(res) {
        let output = "";
        for (let i in res) {
            output += '<div class="row contentDiv">';
            output += '<a href="/project/' + res[i].id + '"}">';
            output += '<div class="col" style="min-height: 180px">';
            output += '<div class="mt-3">' + '</div>';
            output += '<div class="row mt-2">';
            output += '<div class="col-auto">';
            if (res[i].status == '모집중') {
                output += '<button class="btn recruitButton rounded-4 btn-sm fs-16">' + "모집중" + '</button>';
            } else if (res[i].status == '모집완료') {
                output += '<button class="btn unRecruitButton rounded-4 btn-sm fs-16">' + "모집완료" + '</button>';
            }
            output += '</div>';
            output += '<div class="col-auto fw-semibold ps-0 fs-22">' + res[i].title + '</div>';
            output += '<div class="col fs-16 text-end">';
            output += '<span class="ms-2 text-secondary">' + "마감일 |" + '</span>';
            output += '<span class="ms-2 text-secondary">' + formatDateYmd(res[i].startDate) +  '</span>';
            output += '</div>';
            output += '</div>';
            output += '<div class="row mt-2">';
            output += '<div class="col overflow-board fs-18 text-secondary">' + res[i].content + '</div>';
            output += '</div>';
            output += '<div class="row mt-3">';
            output += '<div class="col-auto">';
            output += '<button class="btn btn-sm cateButton fs-18 me-2 text-primary">';
            output += '<span>' + res[i].projectStudy + '</span>';
            output += '</button>';
            for (let p in res[i].positionList) {
                output += '<span class="me-1">';
                output += '<button class="btn btn-sm cateButton fs-18 me-1 fw-medium">' + res[i].positionList[p] +'</button>';
                output += '</span>';
            }
            for (let t in res[i].techList) {
                output += '<button class="btn btn-sm cateButton fw-medium me-2 fs-18">';
                if (res[i].techList[t] == 'Java') {
                    output += '<span class="text-danger me-1">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'Spring' || res[i].techList[t] == 'Spring Boot') {
                    output += '<span class="text-success me-1">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'C') {
                    output += '<span class="me-1" style="color: #007aff;">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'JavaScript') {
                    output += '<span class="me-1" style="color: #712cf9;">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'Python' || res[i].techList[t] == 'TypeScript') {
                    output += '<span class="text-primary me-1">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'React') {
                    output += '<span class="me-1" style="color: #14D3FF;">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'NodeJS') {
                    output += '<span class="me-1" style="color: #9c27b0;">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'Swift') {
                    output += '<span class="me-1" style="color: #b71c1c;">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'Kotlin') {
                    output += '<span class="me-1" style="color: #712cf9;">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'Git' || res[i].techList[t] == 'AWS') {
                    output += '<span class="me-1" style="color: #FF9100">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'Flutter' || res[i].techList[t] == 'Docker' || res[i].techList[t] == 'Django') {
                    output += '<span class="me-1">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'PHP') {
                    output += '<span class="text-secondary me-1">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'MySQL') {
                   output += '<span class="me-1" style="color: #ff6f0f">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'Oracle') {
                   output += '<span class="text-danger me-1">' + "•" + '</span>';
                }
                output += '<span class="ms-1">' + res[i].techList[t] + '</span>';
                output += '</button>';
            }
            output += '</div>';
            output += '</div>';
            output += '<div class="row mt-3">';
            output += '<div class="col fs-18 text-secondary">';
            output += '<span>' + res[i].writer + '</span>';
            output += '<span class="ms-2 me-1">' + "•" + '</span>';
            output += '<span class="ms-1">' + res[i].formattedDate + '</span>';
            output += '</div>';
            output += '<div class="col fs-18 text-secondary text-end">';
            output += '<span class="me-3">';
            output += '<i class="bi bi-bookmark">' + '</i>';
            output += '<span class="ms-1">' + res[i].clipCount + '</span>';
            output += '</span>';
            output += '<span class="me-3">';
            output += '<i class="bi bi-eye">' + '</i>';
            output += '<span class="ms-1">' + res[i].readCount + '</span>';
            output += '</span>';
            output += '<span>';
            output += '<i class="bi bi-chat">' + '</i>';
            output += '<span class="ms-1">' + res[i].commentCount + '</span>';
            output += '</span>';
            output += '</div>';
            output += '</div>';
            output += '<div class="mt-3">' + '</div>';
            output += '</div>';
            output += '</a>';
            output += '<div class="border-bottom border-2">' + '</div>';
            output += '</div>';
        }
        document.querySelector('#project-list').innerHTML = output;
      }, error: function(err) {
             return;
     }
     });
  }




window.addEventListener('scroll', function() {
    var scrollHeight = window.pageYOffset; // 스크롤한 높이 확인
    var targetHeight = 200; // 스크롤을 내려서 sticky를 적용해야하는 높이

    var stickyElements = document.querySelectorAll('.sticky-top'); // 모든 sticky-top 요소 선택
    stickyElements.forEach(function(element) {
        if (scrollHeight > targetHeight) {
            /* 원하는 높이에 도달하면 padding-top 값을 늘린다. */
            element.style.paddingTop = '20px';
            element.style.paddingBottom = '20px';

        } else {
            /* 원하는 높이에 도달하지 않으면 padding-top 값을 초기화 */
            element.style.paddingTop = '0';
        }
    });
});

