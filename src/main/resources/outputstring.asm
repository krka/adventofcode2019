func outputString(s)
  for i = 0, 1 do
    int ch = s[i]
    if ! ch then
      break
    end
    output(ch)
  end
end
