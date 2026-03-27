<div align="center">
  <img src="img/icon.png" width="256" height="256" alt="SocialCredit Stupid Addon logo"/>
  <h1>SocialCredit – Stupid Addon</h1>
</div>

![GitHub Licence](https://img.shields.io/github/license/EvilSquirrelGuy/SocialCredit-StupidAddon?style=plastic&label=licence)
![GitHub last commit](https://img.shields.io/github/last-commit/EvilSquirrelGuy/SocialCredit-StupidAddon?display_timestamp=author&style=plastic)
![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/EvilSquirrelGuy/SocialCredit-StupidAddon/gradle_ci.yml?style=plastic&logo=github)
![Powered by Monster](https://img.shields.io/badge/powered_by-Monster-greenyellow?style=plastic)

[![Modrinth Downloads](https://img.shields.io/modrinth/dt/kNMYucNn?style=plastic&logo=modrinth)](https://modrinth.com/plugin/sc-stupidaddon)
[![Hangar Downloads](https://img.shields.io/hangar/dt/SocialCredit-StupidAddon?style=plastic&label=hangar%20downloads)](https://hangar.papermc.io/EvilSquirrelGuy/SocialCredit-StupidAddon)
![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/EvilSquirrelGuy/SocialCredit-StupidAddon/total?style=plastic&logo=github)

<!-- TODO: Add more fancy badges :) -->

Have you ever contemplated what life would be like if the developer of the [SocialCredit](https://github.com/ZeeRaider/SocialCredit) plugin
was malicious and added the worst features you could possibly imagine to it?

No? Well I did. That's why I decided to create the *'SocialCredit – Stupid Addon'*!

Where SocialCredit will add interesting new totalitarian-flavoured features to your server that make you reconsider how
you do certain things, the Stupid Addon cranks those features up to 11.

## Key 'Features'

Most of the features of this plugin won't really feel coherent to any single theme, apart from one: *'Making **everyone**
suffer'*. With that out of the way, here are the main things that this plugin does:

- [x] Low TPS Punishment – Punish your players for something that isn't really their fault... Server Lag!
- [x] Eye Contact Prevention – Make awkward eye contact a thing of the past!
- [x] Chat Censorship – Are you tired of players screaming SIX SEVENNNN? No? Then never fear, this plugin will solve
      your non-existent issue straight away<sup>1</sup>.
- [ ] Conversation monitoring – A good civilisation is built on communication! That's why you should strive to
      uphold communication with your comrades. Also the state is watching, and rudeness will **not** be tolerated.
- [ ] Player reporting – Do you feel like someone is doing something The Ministry would not approve of? Do you want to
      do your part to protect the communal harmony? Then report your friends to The Ministry today! You might even get
      rewarded for it...<sup>2</sup>
- [ ] Environmental respect enforcement – Are you doing a good enough job of respecting the environment? Are you sure?
      So you're not polluting the oceans? Or defacing the terrain with... *gasps* the **incorrect tool for the job**?!
      Perfect! Then you have absolutely nothing to worry about... or do you?
- [ ]  *idk, other really bad or plain stupid things*

If you have any ideas of your own as to how you can make your players (or server owners) suffer **more**, why not make
an [issue](https://github.com/EvilSquirrelGuy/SocialCredit-StupidAddon/issues) or even a
[pull request](https://github.com/EvilSquirrelGuy/SocialCredit-StupidAddon/pulls)!

***
<sup>1</sup> or make it worse...

<sup>2</sup> this *might* become something that's better suited to the core plugin, so may get removed/indefinitely
delayed


## Actual Features

In case you'd rather know the *how* rather than the *what*, don't worry, i gotchu bro. This is a list of how the plugin
does everything so well! ~~(Or realistically, so badly, but shhhh.)~~

- ~~Easy~~ *Horrible* configuration system that uses HTML-as-a-config. You'll end up settling for the defaults!
- *Modular* module API. That means you can actually extend the plugin without (too much) pain!


## How Do I Use This?

So you've decided to download this plugin for your server. First of all, I'd like to offer you my condolences for your
impending loss (of sanity). Here's what you need to do to get it up and running in no time (or realistically anywhere
from 60 seconds to 60 hours):

1. Make sure you actually have all the correct software. You need _all_ of these things for this to work:
    - A working **Paper** server (1.21.1+ recommended, should work on 1.20.6+)
    - The [SocialCredit](https://github.com/ZeeRaider/SocialCredit) Core plugin installed on your server
2. Once you're absolutely sure you have all those things, download the latest version of the SocialCredit – Stupid Addon
   from the GitHub releases tab, Modrinth, or Hangar.
3. Put it in the `plugins/` folder on your server.
4. Restart it
5. Congratulations, the Stupid Addon is now loaded! You can be proud of yourself now!
6. _(Optional)_ Edit the config file. You can find it at `plugins/SocialCredit-StupidAddon/config.html`. If you're
   struggling with the format, go read [Configuration](#configuration).


## FAQs
**AKA**: _'Questions that literally no-one would ever ask'_.

What?
> A stupid addon. For adding stupid features. It's stupid.

Why?
> Because yes. It's funny.

How?
> In IntelliJ, while procrastinating writing my Bachelor's thesis...

Who?
> Tao!

When?
> 2026, or whatever year it is now

Where?
> Not telling :p


## Configuration

This plugin is configured using HTML. Yes. HTML.

Why? Because I felt like it. There is genuinely no other reason.

The configuration system uses my amazing purpose-build library [JHAAC](https://github.com/EvilSquirrelGuy/JHaaC), so you
might want to go check that in case your config isn't working.

With that out of the way, here's how the config system works, in a nutshell.

### Guide

All configuration happens inside `main#config` inside a HTML document ([config.html](src/main/resources/config.html)), so
any content outside of it is irrelevant. That means you can customise the page to your heart's content, and it won't
detract from your ability to configure the plugin! Pretty neat, right?

Below is a sample of what a basic configuration file using the custom HTML syntax might look like.

```html

<main id="config">
  <section id="example-group">
    <input name="example-text" type="text" value="This is an example!" />
    <input name="example-numeric" type="number" value="420" />
    <input name="example-boolean" type="checkbox" checked />
    <select name="example-list">
      <option value="This is an example value"></option>
      <option value="696969"></option>
    </select>
  </section>
  <section id="other-group">
    <input name="other-text" type="text" value="You get the picture..." />
  </section>
</main>

```

As you can see, it uses semantic HTML to (loosely) describe the function of each field:
- `<main>` is the root element
- `<section>`s are config groups/sections
- `<input>`s are values (I swear it makes sense)
- `<select>`-`<option>`s are lists of values

In all fairness, it's not the nicest thing to read, or to edit. However, that was a large part of why I chose this
config system! Once you get the hang of it (and remember to open it in a text editor, not a browser) it's very
straightforward, and you'll be able to customise config options in no time!

If you're planning on extending the config for any contributions you make, you should probably note some of the...
weirdness introduced by the config system:

1. Boolean values are stored using the `checked` attribute. Its presence implies true, its absence implies false.
   What does this mean for you? Basically, you can *technically* read text config fields as false booleans...
   not ideal. This might be fixed in a future release of JHaaC, but I wouldn't count on it.
2. As a follow-up to point 1, you can't _officially_ store booleans in lists, since the `<option>` tag doesn't support
   the `checked` attribute. Realistically you _can probably_ get away with it – but no guarantees that it'll work 
   forever.

As an end user, none of these apply to you. Except maybe the info on how to store boolean values. Yes, that might be
relevant. 


## Gallery

<img src="img/lag-detect.png" alt="lag detection module" width="500">
<img src="img/eye-contact.png" alt="eye contact module" width="500">
<img src="img/chat-censor.png" alt="chat censorship module" width="500">


## Developing

### Prerequisites & Setup

If you want to develop the project for some reason (idk why you would), make sure you have the following installed:

- Git (or something that lets you interact with the VCS)
- Java 21 **JDK** (Note: The JRE won't work)
- Any IDE that can edit Java. This project is configured to use IntelliJ, so you might just want to use that.
- A brain

Once you have those, clone the repository to somewhere on your system. You probably want this location to be easily
accessible (or not, I won't judge your 20 levels of nested folders). You can use the following command:

```shell
git clone https://github.com/EvilSquirrelGuy/SocialCredit-StupidAddon.git
```

<details>
<summary>If you have the GitHub CLI installed</summary>

The GitHub CLI provides a shorter command you can use to clone the repo. This one:

```shell
gh repo clone EvilSquirrelGuy/SocialCredit-StupidAddon
```

</details>

Once you've cloned the repo, you can start developing! Just kidding, you have to set up the project first.

### Dependencies

The project uses packages from GitHub's package repository, which you need a token to access, go make one
[here](https://github.com/settings/tokens). I'd suggest **not** committing your secrets to Git – it's bad practice.
Instead, create `~/.gradle/gradle.properties` and add the following lines to it (obviously replace the placeholders).

```properties
gpr.user=YourGitHubUsername
gpr.token=ghp_USEYOURPERSONALACCESSTOKENTHATYOUJUSTGENERATED
```

But wait, you're not done yet! Well, you might be, but just make sure that the latest SocialCredit JAR is in the `lib/`
directory. It should be, it's on VCS, but just go check just in case.

I usually let IntelliJ do all my gradle stuff for me, but in case you're using something else, you can just build the
project with gradle to resolve and download dependencies:

```shell
./gradlew build
```
*If you're on windows, replace `./gradlew` with `.\gradlew.bat`, because Windows is just special like that.

If you're using IntelliJ, you can just open the gradle tab and click the download icon. Then you can just run the build
task through your IDE, it's really that easy.

Also, ignore the errors about `shadowJar` in `build.gradle.kts`, they're not real. They are genuinely fake, IntelliJ is
just gaslighting you.

### Contributing

There, now you're almost ready to contribute. But there's one last step you need to do first. Go read the code, learn
the code, familiarise yourself with the code. Also the code style, that's important too.

Go see [CONTRIBUTING.md](CONTRIBUTING.md) for more info on what's expected of you when you write code.

The plugin uses a modular module-loading system, so it should be *pretty easy* to add your own modules. In fact, it's
so easy, that I can just make a small guide here... but I won't.

Once you've finished writing your new code, make a pull request and let the CI check if your code even compiles. Granted,
this is a pretty low bar, most of my commits build, half of them don't actually work, and there's no unit tests to check
that... oops?

Then you can pat yourself on the back for contributing to an open-source project (which is just a stupid addon that
no-one will use but shhh)!


## Licence

The necessarily boring, and boringly necessary bit.

This project and all its source files are protected by the Mozilla Public License, version 2.0. In a nutshell this
means that any changes you make to the files in this project must be released under the same licence (publicly, of
course), same goes for whatever code you might use from here.

This isn't legal advice though. And what you're _bound by_ is the actual licence text itself, so you may want to go and
read the [licence file](LICENCE), or check out [choosealicense.com](https://choosealicense.com/licenses/mpl-2.0/).

Or just go [ask chatgpt](chatgpt.com/?q=explain+the+terms+of+the+mpl+2.0+like+im+completely+stupid+and+know+nothing) to
explain it to you if you're feeling extra lazy.



## Interesting but irrelevant bits

<details>
<summary>Unimplemented/Scrapped Features</summary>

This section details some features that will never make it into the plugin. Nonetheless, it might be interesting to see
just how bad this plugin could have been if I had wanted/been able to implement them.

- Malbolge Config file – no. just no. it would literally be **impossible** to configure anything.
  Side note: this could also be combined with the HTML config, and force users to generate malbolge code for any
  configuration value they want to use. but why...
- Brainfuck Config file – while brainfuck isn't _as_ bad as the above, still a no. also introduces security risks ig, but
  you'd have to be fucking mental to write malware in brainfuck.
- Lag-unfriendly mode! – i'm too lazy to write _two separate versions of everything_ where one of the things is
  _intentionally unoptimised_. count your blessings i guess? 

</details>
