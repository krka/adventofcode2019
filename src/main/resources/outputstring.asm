func outputString(s)
  int i = 0
  while 1
    int ch = s[i]
    if ! ch then
      break
    end
    output(ch)
    i = i + 1
  end
end
