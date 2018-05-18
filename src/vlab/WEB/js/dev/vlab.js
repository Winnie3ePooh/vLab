function init_lab() {
    var parsed = document.getElementsByTagName('script')[2].src.split('/');
    var userCode;
    parsed.pop();
    parsed.push('src');
    parsed = parsed.join('/');
    requirejs.config({paths: {ace: parsed}});
    var container,
        window = '<div class="container">'+
            '      <div class="row header">'+
            '        <div class="col col-8">'+
            '          <button class="btn btn-primary btn-xs" type="button">?????</button>'+
            '          <button class="btn btn-primary btn-xs" id="checkSolution" type="button">Проверить</button>'+
            '          <button class="btn btn-primary btn-xs" type="button">?????????</button>'+
            '        </div>'+
            '        <div class="col col-3">'+
            '          <button class="btn btn-dark btn-block" type="button">??????</button>'+
            '        </div>'+
            '        <div class="col col-1 d-flex align-items-center justify-content-center">'+
            '          <div>'+
            '            <span class="oi oi-question-mark"></span>'+
            '          </div>'+
            '        </div>'+
            '      </div>'+
            '      <div class="row main-part">'+
            '        <div class="col col-9">'+
            '          <div><pre id="editor"></pre>'+
            '</div>'+
            '        </div>'+
            '        <div id="task-text" class="col col-3">'+
            '          <p id="inputText"></p>'+
            '          <p id="outputText"></p>'+
            '        </div>'+
            '      </div>'+
            '      <div class="row console">'+
            '        <div class="col col-10">'+
            '          <div class="row">'+
            '            <div class="col col-9" id="runResults">' +
            '               <pre class="prettyprint" id="results">'+
            '               </pre></div>'+
            '            <div class="col col-3">2</div>'+
            '          </div>'+
            '        </div>'+
            '        <div class="col col-2">3</div>'+
            '      </div>'+
            '    </div>';

    function init_task() {
        var data = get_variant();
        document.getElementById('inputText').innerText = data.inputDataText;
        document.getElementById('outputText').innerText = data.outputDataText;
        var variantble = 'function foo(items) {\n'+
            '   var i;\n'+
            '   for (i = 0; i < items.length; i++) {\n'+
            '       alert("Ace Rocks " + items[i]);\n'+
            '   }\n}';
        requirejs(['ace/ace'], function(ace){
            var editor = ace.edit("editor");
            editor.setTheme("ace/theme/twilight");
            editor.session.setMode("ace/mode/javascript")
            editor.session.setValue(variantble);
        });
    }

    function get_variant() {
        var variant;
        if ($("#preGeneratedCode") !== null) {
            variant = JSON.parse($("#preGeneratedCode").val());
        } else {
            //init_editor_now();
            variant = JSON.parse($("#preGeneratedCode").val());
        }
        return variant;
    }

    function check_solution() {
        requirejs(['ace/ace'], function(ace){
            var editor = ace.edit("editor");
            userCode = editor.getValue();
            ANT.calculate();
        });
    }

    function init_editor_now() {

    }

    return {
        init: function () {
            container = $("#jsLab")[0];
            container.innerHTML = window;
            init_task();
            $("#checkSolution").on('click', function(){
                check_solution();
            });
        },
        calculateHandler: function (text, code) {

            console.log('Я ВЕРНУЛСЯ');
            console.log(text);
            console.log(unescape(JSON.parse(code)));
        },
        getResults: function () {
            return "results";
        },
        getCondition: function (cd) {
            var condition;
            console.log(cd);
            condition = {
                "S": userCode
            };
            return JSON.stringify(condition);
        }
    }
}

var Vlab = init_lab();