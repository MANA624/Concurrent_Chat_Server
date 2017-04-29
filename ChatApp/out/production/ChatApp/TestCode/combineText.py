import io

alltext = []
for i in range(1, 7):
	#if i == 5:
	#	continue
	reading = io.open('Episode'+str(i)+'.txt', 'r', encoding='utf-8')
	appending = ""
	for char in reading.read():
		try:
			appending += char
		except:
			pass
	alltext.append(appending)
	reading.close()

final = " ".join(alltext)

writing = open('AllEpisodes.txt', 'w')
writing.write(final)
writing.close()
