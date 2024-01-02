#! /usr/bin/env node
const {program} = require('commander');
const mutate = require('./mutate.js');
const test = require('./test.js')
// const shell = require('shelljs');
// const jar_dir = '../target/mutest-demo-1.0-SNAPSHOT-jar-with-dependencies.jar';
// let testsuite = '../testsuite';
// let pool = '"../pool"';


// mutate命令
program.command('mutate')
    .description('Generate the mutants.')
    .argument('<mutator>', 'choose a mutator')
    .action(mutate);

// test命令
program.command('test')
    .description('Test the mutants.')
    .argument('<test_class>', 'the class to test the mutants.')
    .argument('[test_dir]', 'the dir where contains the .class file of the testsuite.', 'testsuite')
    .action(test);

program.parse();