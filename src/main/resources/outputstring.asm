int s = 0
int i = 0
int ch = 0

func outputString sptr
s = sptr + 0
i = 0 + 0
label loop
getarrayptr s i ch
jumpfalse ch finish
output ch
i = i + 1
jumpfalse 0 loop
label finish
return
endfunc
