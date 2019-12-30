include division.asm

var N
var D
var Q
var R

label loop
input N
input D
call div N D : Q R
output Q
output R
jumptrue 1 loop
