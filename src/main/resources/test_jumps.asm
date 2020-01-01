if 1 < 1 jump fail
if 2 < 1 jump fail

if 1 <= 0 jump fail

if 0 == 1 jump fail

if 1 != 1 jump fail

if 1 > 1 jump fail
if 1 > 2 jump fail

if 0 >= 1 jump fail

if 0 <= 1 jump step1
jump fail

step1:
if 1 <= 1 jump step2
jump fail

step2:
if 1 >= 1 jump step3
jump fail

step3:
  output(1)
  halt

fail:
  output(0)
  halt
