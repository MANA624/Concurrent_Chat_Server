import numpy as np
import matplotlib.pyplot as plt

N = 3
threaded_mean = (1040, 1076, 1528)
naive_mean = (80, 174.2, 1049.4)

ind = np.arange(N)  # the x locations for the groups
width = 0.35       # the width of the bars

fig = plt.figure()
ax = fig.add_subplot(111)
thread_rects = ax.bar(ind, threaded_mean, width, color='r')
naive_rects = ax.bar(ind+width, naive_mean, width, color='c')

# add some
ax.set_ylabel('Time (ms)')
ax.set_xlabel('Size of message from a few words to Star Wars script')
ax.set_title('Average Runtime for 1 simulated user')
ax.set_xticks(ind+width)
ax.set_xticklabels(('Small', 'Medium', 'Large'))

ax.legend((thread_rects[0], naive_rects[0]), ('Threaded', 'Naive'))
plt.show()


N = 3
threaded_mean = (2001, 2345.8, 4270.8)
naive_mean = (80, 597.2, 3499.2)

ind = np.arange(N)  # the x locations for the groups
width = 0.35       # the width of the bars

fig = plt.figure()
ax = fig.add_subplot(111)
thread_rects = ax.bar(ind, threaded_mean, width, color='r')
naive_rects = ax.bar(ind+width, naive_mean, width, color='c')

# add some
ax.set_ylabel('Time (ms)')
ax.set_xlabel('Size of message from a few words to Star Wars script')
ax.set_title('Average Runtime for 2 simulated users')
ax.set_xticks(ind+width)
ax.set_xticklabels(('Small', 'Medium', 'Large'))

ax.legend((thread_rects[0], naive_rects[0]), ('Threaded', 'Naive'))

plt.show()


N = 3
threaded_mean = (4002.4, 5467.4, 12736.6)
naive_mean = (80.8, 2113.8, 13929.2)

ind = np.arange(N)  # the x locations for the groups
width = 0.35       # the width of the bars

fig = plt.figure()
ax = fig.add_subplot(111)
thread_rects = ax.bar(ind, threaded_mean, width, color='r')
naive_rects = ax.bar(ind+width, naive_mean, width, color='c')

# add some
ax.set_ylabel('Time (ms)')
ax.set_xlabel('Size of message from a few words to Star Wars script')
ax.set_title('Average Runtime for 4 simulated users')
ax.set_xticks(ind+width)
ax.set_xticklabels(('Small', 'Medium', 'Large'))

ax.legend((thread_rects[0], naive_rects[0]), ('Threaded', 'Naive'))

plt.show()


N = 3
threaded_mean = (8009.8, 13587.4, 37415)
naive_mean = (83, 8069.2, 55633.2)

ind = np.arange(N)  # the x locations for the groups
width = 0.35       # the width of the bars

fig = plt.figure()
ax = fig.add_subplot(111)
thread_rects = ax.bar(ind, threaded_mean, width, color='r')
naive_rects = ax.bar(ind+width, naive_mean, width, color='c')

# add some
ax.set_ylabel('Time (ms)')
ax.set_xlabel('Size of message from a few words to Star Wars script')
ax.set_title('Average Runtime for 8 simulated users')
ax.set_xticks(ind+width)
ax.set_xticklabels(('Small', 'Medium', 'Large'))

ax.legend((thread_rects[0], naive_rects[0]), ('Threaded', 'Naive'))

plt.show()
