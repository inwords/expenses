import content from './content';
import {AllContent, ContentLanguages} from '@/6-shared/types';

export const useContent = (): AllContent[ContentLanguages] => {
  return content['ru'];
};
