const {program} = require('commander');
const shell = require('shelljs');
let opts = program.opts();
const jar_dir = 'target/mutest-demo-1.0-SNAPSHOT-jar-with-dependencies.jar';

function mutate(mutator) {
    let src = '"src/main/java/edu/nju/mutest/example/Calculator.java"';
    let pool = '"pool"';
    // shell.cd('../');
    shell.exec(`java -jar ${jar_dir} ${src} ${pool} ${mutator}`);
    shell.exec(`compile-mutants.sh ${pool}`);
}

module.exports = mutate;