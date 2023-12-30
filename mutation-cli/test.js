const {program} = require('commander');
const shell = require('shelljs');
let opts = program.opts();
const jar_dir = 'target/mutest-demo-1.0-SNAPSHOT-jar-with-dependencies.jar';

function test(){
    let testsuite = 'testsuite';
    let pool = '"pool"';
    shell.exec(`java -cp ${jar_dir} edu.nju.mutest.DemoMutantExecution ${testsuite} ${pool}`)
}

module.exports = test