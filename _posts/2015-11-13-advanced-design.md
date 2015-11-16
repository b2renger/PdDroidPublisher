---
layout: post
title:  "Advanced design"
permalink: advanced-design
---

This guide is about the customization of the looks and behavior of gui components. It implies using the PdDroidPartyConfig class that will get us access great cutomisation possibilities. 


<ul>
{% assign sorted_posts = (site.posts | sort: 'order', 'first') %}
{% for post in sorted_posts %}
{% if post.tags contains 'advanced-design' %}
  <li>
      <a class="post-link" href="{{ post.url | prepend: site.baseurl }}">{{ post.title }}</a>
  </li>
{% endif %}
{% endfor %}
</ul>