const {program} = require('commander');
const shell = require('shelljs');
let opts = program.opts();
const jar_dir = 'target/mutest-demo-1.0-SNAPSHOT-jar-with-dependencies.jar';

function test(test_class, test_dir){
    let pool = '"pool"';
    shell.cd('../')
    shell.exec(`java -cp ${jar_dir} edu.nju.mutest.DemoMutantExecution ${test_dir} ${pool} ${test_class}`);
}

module.exports = test;