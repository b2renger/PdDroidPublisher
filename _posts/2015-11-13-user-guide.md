---
layout: post
title:  "User Guide"
permalink: user-guide
---

TODOC : links to user guide pages (jamming over wifi, ...etc)

<ul>
{% assign sorted_posts = (site.posts | sort: 'order', 'first') %}
{% for post in sorted_posts %}
{% if post.tags contains 'user-guide' %}
  <li>
      <a class="post-link" href="{{ post.url | prepend: site.baseurl }}">{{ post.title }}</a>
  </li>
{% endif %}
{% endfor %}
</ul>
