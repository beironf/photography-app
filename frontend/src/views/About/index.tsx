import { Avatar, Typography } from '@mui/material';
import { ImageApi } from 'api/ImageApi';
import React from 'react';
import { theme } from 'style/theme';

export const About: React.FunctionComponent = () => {
  const MS_IN_ONE_DAY = 1000 * 60 * 24;
  const MY_BIRTHDAY = new Date('3/7/1994');

  const ageInMs = Date.now() - MY_BIRTHDAY.getTime() + MS_IN_ONE_DAY;
  const ageDate = new Date(ageInMs);
  const myAge = Math.abs(ageDate.getUTCFullYear() - 1970);

  return (
    <div
      style={{
        textAlign: 'center',
        maxWidth: theme.aboutContentMaxWidth,
        margin: '0 auto',
        padding: `0 ${theme.primaryPadding}px`,
      }}
    >
      <Avatar
        alt="Portrait"
        src={ImageApi.SiteImageRoute.getSiteImageUrl('portrait.jpg')}
        sx={{
          width: theme.aboutAvatarSize,
          height: theme.aboutAvatarSize,
          margin: `0 auto ${theme.primaryPadding}px`,
        }}
      />
      <Typography
        variant={theme.aboutBodyTextVariant as any}
        style={{ padding: `0 ${theme.primaryPadding}px ${theme.primaryPadding}px` }}
        textAlign="center"
      >
        Hi! My name is Fredrik. I&apos;m
        {` ${myAge} `}
        years old and live in Gothenburg, Sweden.
        <br />
        Welcome to my photography site.
        <br />
        <br />
        Photography is one of my main hobbies in life, especially landscape photography.
        I get most inspired by the views and landscapes I don&apos;t see everyday.
        That&apos;s why I love to shoot when going on trips to new places. The nature is
        a central part that I&apos;m drawn to and to be able to remember certain moments
        far later by looking at an image is very powerful.
      </Typography>
      <img
        src={ImageApi.SiteImageRoute.getSiteImageUrl('portrait_photographer.jpg')}
        alt="Photographer Portrait"
        style={{ width: '100%', marginBottom: `${theme.primaryPadding}px` }}
      />
      <Typography
        variant={theme.aboutSubtitleVariant as any}
        style={{ padding: `${theme.primaryPadding}px` }}
        textAlign="center"
      >
        My Gear
      </Typography>
      <Typography
        variant={theme.aboutBodyTextVariant as any}
        style={{ padding: `0 ${theme.primaryPadding}px ${theme.primaryPadding}px` }}
        textAlign="center"
      >
        I have previously owned two Canon cameras; EOS 600D and 5D Mark II. Combined with large
        lenses this was quite cumbersome when traveling. I noticed that I started taking more
        photos when switching to my current Fujifilm x100f with a fixed 23mm lens because it was
        way smaller and easier to bring. It&apos;s a bit limiting without a proper zoom lens, but
        for most cases it works really well and challenges me to find creative compositions.
        <br />
        <br />
        I also bought a complementary DJI Mini 3 drone before my trip to Australia to capture new
        types of views from the sky.
      </Typography>
      <img
        src={ImageApi.SiteImageRoute.getSiteImageUrl('drone_remote.jpg')}
        alt="Drone Remote"
        style={{ width: '100%', marginBottom: `${theme.primaryPadding}px` }}
      />
      <Typography
        variant={theme.aboutSubtitleVariant as any}
        style={{ padding: `${theme.primaryPadding}px` }}
        textAlign="center"
      >
        The Website
      </Typography>
      <Typography
        variant={theme.aboutBodyTextVariant as any}
        style={{
          padding: `0 ${theme.primaryPadding}px ${theme.primaryPadding}px`,
          marginBottom: `${theme.primaryPadding}px`,
        }}
        textAlign="center"
      >
        Most of my previous attempts to build a website from scratch have been educational
        but not led to any real usage. It&apos;s been a hobby for many years because it allows
        me to be creative and produce something instead of only consuming content which
        is so common these days.
        <br />
        <br />
        The idea of joining two of my main hobbies, photography and coding,
        has also been around for some time. It was first after I&apos;ve worked professionally
        as a software developer for a few years that I completed that vision.
        <br />
        <br />
        I know there are a lot of services out there that can help you put up your own
        photography website very easily. That would be the best choice if I just wanted a website,
        but then I would miss out on the journey of building it and also the flexibility
        to make it work exactly as I want.
        <br />
        <br />
        The goal is that this will be an ongoing project and that more views and features
        will be added to allow me showing the best of my photography!
      </Typography>
    </div>
  );
};
