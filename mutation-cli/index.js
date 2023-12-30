#! /usr/bin/env node
const {program} = require('commander');
const mutate = require('./mutate')

// mutate命令
program.command('mutate').action(mutate)
// 定义action
program.parse()