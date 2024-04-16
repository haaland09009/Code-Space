  const reloadProjectList = (techIdList) => {
    $.ajax({
       type: "get",
       url: "/project/list",
       data: {
           techIdList: techIdList.length > 0 ? techIdList : []
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
                output += '<button class="btn unRecruitButton rounded-4 btn-sm fs-16">' + "모집중" + '</button>';
            }
            output += '</div>';
            output += '<div class="col-auto fw-semibold ps-0 fs-22">' + res[i].title + '</div>';
            output += '<div class="col fs-16 text-end">';
            output += '<span class="ms-1 text-secondary">' + "마감일 |" + '</span>';
            output += '<span class="ms-1 text-secondary">' + res[i].startDate +  '</span>';
            output += '</div>';
            output += '</div>';
            output += '<div class="row mt-2">';
            output += '<div class="col overflow-board fs-18 text-secondary">' + res[i].content + '</div>';
            output += '</div>';
            output += '<div class="row mt-3">';
            output += '<div class="col-auto">';
            output += '<button class="btn btn-sm cateButton fs-18 me-1 text-primary">';
            output += '<span>' + res[i].projectStudy + '</span>';
            output += '</button>';
            for (let p in res[i].positionList) {
                output += '<span>';
                output += '<button class="btn btn-sm cateButton fs-18 me-1 fw-medium">' + res[i].positionList[p] +'</button>';
                output += '</span>';
            }
            for (let t in res[i].techList) {
                output += '<button class="btn btn-sm cateButton fw-medium me-1 fs-18">';
                if (res[i].techList[t] == 'Java') {
                    output += '<span class="text-danger">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'Spring' or res[i].techList[t] == 'Spring Boot') {
                    output += '<span class="text-success">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'C') {
                    output += '<span style="color: #007aff;">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'Python' or res[i].techList[t] == 'TypeScript') {
                    output += '<span class="text-primary">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'React') {
                    output += '<span style="color: #14D3FF;">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'NodeJS') {
                    output += '<span style="color: #9c27b0;">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'Swift') {
                    output += '<span style="color: #b71c1c;">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'Kotlin') {
                    output += '<span style="color: #712cf9;">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'Git' or res[i].techList[t] == 'AWS') {
                    output += '<span style="color: #FF9100">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'Flutter' or res[i].techList[t] == 'Docker' or res[i].techList[t] == 'Django') {
                    output += '<span>' + "•" + '</span>';
                } else if (res[i].techList[t] == 'PHP') {
                    output += '<span class="text-secondary">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'MySQL') {
                   output += '<span style="color: #ff6f0f">' + "•" + '</span>';
                } else if (res[i].techList[t] == 'Oracle') {
                   output += '<span class="text-danger">' + "•" + '</span>';
                }
                output += '<span class="ms-1">' + res[i].techList[t] + '</span>';
                output += '</button>';
            }
            output += '</div>';
            output += '</div>';
            output += '<div class="row mt-3">';
            output += '<div class="col fs-18 text-secondary">';
            output += '<span>' + res[i].writer + '</span>';
            output += '<span class="ms-1">' + "•" + '</span>';
            output += '<span>' + res[i].formattedDate + '</span>';
            output += '</div>';
            output += '<div class="col fs-18 text-secondary text-end">';
            output += '<span class="me-2">';
            output += '<i class="bi bi-bookmark">' + '</i>';
            output += '<span>' + res[i].clipCount + '</span>';
            output += '</span>';
            output += '<span class="me-2">';
            output += '<i class="bi bi-eye">' + '</i>';
            output += '<span>' + res[i].readCount + '</span>';
            output += '</span>';
            output += '<span>';
            output += '<i class="bi bi-chat">' + '</i>';
            output += '<span>' + res[i].commentCount + '</span>';
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
